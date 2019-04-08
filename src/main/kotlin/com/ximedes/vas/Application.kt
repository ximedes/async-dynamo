package com.ximedes.vas

import com.ximedes.vas.domain.Account
import com.ximedes.vas.domain.AccountID
import com.ximedes.vas.domain.Transfer
import com.ximedes.vas.domain.UserID
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
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

data class NewAccountMessage(val account: String, val user: String, val overdraft: Long, val description: String)

data class TransferMessage(val from: String, val to: String, val amount: Long, val description: String)

fun Application.module() {
    val logger = KotlinLogging.logger {}

    install(ContentNegotiation) {
        jackson { }
    }

    val ledger = Ledger()
    runBlocking {
        ledger.init()
    }

    routing {
        route("/account") {
            post {
                val msg = call.receive<NewAccountMessage>()
                val account = Account(UserID(msg.user), AccountID((msg.account)), 0, msg.overdraft, msg.description)
                ledger.createAccount(account)
                call.respond(HttpStatusCode.OK)

            }
            get("/{userId}") {
                val userID = UserID(call.parameters["userId"]!!)
                call.respond(ledger.queryUserAccounts(userID))
            }
        }
        post("/transfer") {
            val msg = call.receive<TransferMessage>()
            val transfer = Transfer(AccountID(msg.from), AccountID(msg.to), msg.amount, msg.description)
            ledger.transfer(transfer)
            call.respond(HttpStatusCode.OK)
        }

    }

}

