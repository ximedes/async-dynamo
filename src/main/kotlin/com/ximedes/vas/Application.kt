package com.ximedes.vas

import com.ximedes.vas.dynamo.dsl.writeTransaction
import com.ximedes.vas.dynamo.initTables
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import java.time.Instant
import java.util.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

data class NewAccountMessage(val account: String
                             , val user: String
                             , val overdraft: Int
                             , val description: String)

data class TransferMessage(val fromUser: String
                           , val toUser: String
                           , val from: String
                           , val to: String
                           , val amount: Int)

fun Application.module() {
    val logger = KotlinLogging.logger {}

    val client = DynamoDbAsyncClient.builder()
        .credentialsProvider(ProfileCredentialsProvider.create())
            .region(Region.EU_WEST_1)
            .build()

    runBlocking {
        initTables(client)
    }

    install(ContentNegotiation) {
        jackson { }
    }

    routing {
        get("/") {
            call.respondText { "Hello, World" }
        }
        post("/account") {
            val newAccountMessage = call.receive<NewAccountMessage>()
            client.writeTransaction {
                put("ledger") {
                    item {
                        "pk" from "USER-${newAccountMessage.user}"
                        "sk" from "ACC-${newAccountMessage.account}"
                        "overdraft" from newAccountMessage.overdraft
                        "headroom" from newAccountMessage.overdraft
                        "description" from newAccountMessage.description
                    }
                    condition("attribute_not_exists(sk)")
                }

            }
            call.respond(HttpStatusCode.OK)

        }
        post("/transfer") {
            val transferMessage = call.receive<TransferMessage>()
            val timeStamp = Instant.now().toEpochMilli()
            val id = UUID.randomUUID().toString()

            client.writeTransaction {
                update("ledger") {
                    key {
                        "pk" from "USER-${transferMessage.fromUser}"
                        "sk" from "ACC-${transferMessage.from}"

                    }
                    update("SET headroom = headroom - :a")

                    condition("headroom >= :a")
                    attributes {
                        ":a" from transferMessage.amount
                    }

                }
                update("ledger") {
                    key {
                        "pk" from "USER-${transferMessage.toUser}"
                        "sk" from "ACC-${transferMessage.to}"

                    }
                    update("SET headroom = headroom + :a")
                    attributes {
                        ":a" from transferMessage.amount
                    }

                }
                put("ledger") {
                    item {
                        "pk" from "ACC-${transferMessage.from}"
                        "sk" from "TX-$timeStamp-$id"
                        "id" from id
                        "type" from "DEBIT"
                        "amount" from transferMessage.amount
                    }

                }
                put("ledger") {
                    item {
                        "pk" from "ACC-${transferMessage.to}"
                        "sk" from "TX-$timeStamp-$id"
                        "id" from id
                        "type" from "CREDIT"
                        "amount" from transferMessage.amount
                    }
                }
            }
            call.respond(HttpStatusCode.OK)
        }

    }

}

