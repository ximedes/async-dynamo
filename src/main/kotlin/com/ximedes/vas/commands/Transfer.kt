package com.ximedes.vas.commands

import com.ximedes.vas.Ledger
import com.ximedes.vas.TransferMessage
import com.ximedes.vas.dsl.writeTransaction
import java.time.Instant
import java.util.*

suspend fun Ledger.transfer(msg: TransferMessage) {
    val timeStamp = Instant.now().toEpochMilli()
    val id = UUID.randomUUID().toString()

    client.writeTransaction {
        update("ledger") {
            key {
                "pk" from "USER-${msg.fromUser}"
                "sk" from "ACC-${msg.from}"

            }
            update("SET headroom = headroom - :a")

            condition("headroom >= :a")
            attributes {
                ":a" from msg.amount
            }

        }
        update("ledger") {
            key {
                "pk" from "USER-${msg.toUser}"
                "sk" from "ACC-${msg.to}"

            }
            update("SET headroom = headroom + :a")
            attributes {
                ":a" from msg.amount
            }

        }
        put("ledger") {
            item {
                "pk" from "ACC-${msg.from}"
                "sk" from "TX-$timeStamp-$id"
                "id" from id
                "type" from "DEBIT"
                "amount" from msg.amount
            }

        }
        put("ledger") {
            item {
                "pk" from "ACC-${msg.to}"
                "sk" from "TX-$timeStamp-$id"
                "id" from id
                "type" from "CREDIT"
                "amount" from msg.amount
            }
        }
    }

}