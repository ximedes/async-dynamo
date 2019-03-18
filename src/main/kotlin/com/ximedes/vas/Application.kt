package com.ximedes.vas

import com.ximedes.vas.dynamo.dsl.writeTransaction
import com.ximedes.vas.dynamo.initTables
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.jackson.jackson
import io.ktor.request.receive
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

data class NewAccountMessage(val account: String
                             , val user: String
                             , val overdraft: Int
                             , val description: String)

fun Application.module() {
    val logger = KotlinLogging.logger {}

    val client = DynamoDbAsyncClient.builder()
            .credentialsProvider(DefaultCredentialsProvider.create())
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
                    }
//                    item {
//                        "sk" from "ACC-${newAccountMessage.account}"
//                    }
//                    item {
//                        "overdraft" from newAccountMessage.overdraft
//                    }
//                    item {
//                        "description" from newAccountMessage.description
//                    }
                }

            }

        }
    }
}

