package com.ximedes.vas.api.async

import com.ximedes.vas.api.*
import com.ximedes.vas.domain.*
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing
import kotlinx.coroutines.delay
import mu.KotlinLogging
import java.util.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)


fun Application.module() {
    val logger = KotlinLogging.logger {}

    install(ContentNegotiation) {
        jackson { }
    }

    val ledger = AsyncLedger()

    routing {
        post("/user") {
            val msg = call.receive<CreateUserRequest>()
            val user = User(UserID(msg.id), msg.email)
            ledger.createUser(user)
            call.respond(HttpStatusCode.OK)
        }
        route("/account") {
            get("/{userId}") {
                val userID = UserID(call.parameters["userId"]!!)
                val accounts = ledger.findAccountsByUserID(userID)
                call.respond(accounts)
            }
            post {
                val msg = call.receive<CreateAccountRequest>()
                val account = Account(UserID(msg.user), AccountID((msg.account)), 0, msg.overdraft, msg.description)
                ledger.createAccount(account)
                call.respond(HttpStatusCode.OK)
            }
        }
        post("/transfer") {
            val msg = call.receive<TransferRequest>()
            val id = TransferID(UUID.randomUUID().toString())
            val transfer = Transfer(id, AccountID(msg.from), AccountID(msg.to), msg.amount, msg.description)
            ledger.transfer(transfer)
            call.respond(transfer)
        }
        post("/reset") {
            val reset = call.receive<ResetRequest>()
            val capacity = reset.readCapacityUnits?.let { rcu ->
                reset.writeCapacityUnits?.let { wcu ->
                    Pair(rcu, wcu)
                }
            }
            ledger.reset(capacity)
            call.respond(HttpStatusCode.OK)
        }
        get("/sleep") {
            val ms = call.request.queryParameters["ms"]?.toLong() ?: defaultSleepMS
            delay(ms)
            call.respond(HttpStatusCode.OK)
        }
    }

}

