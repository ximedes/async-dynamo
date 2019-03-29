package com.ximedes.vas.commands

import com.ximedes.vas.Ledger
import com.ximedes.vas.domain.Transfer
import com.ximedes.vas.dsl.query
import com.ximedes.vas.dsl.writeTransaction
import java.time.Instant
import java.util.*

suspend fun Ledger.transfer(transfer: Transfer) {
    val timeStamp = Instant.now().toEpochMilli()
    val id = UUID.randomUUID().toString()

    val fromUser = client.query("ledger") {
        useIndex("accounts")
        keyCondition("sk = :accountID and begins_with(pk, :user)")
        attributes {
            ":accountID" from transfer.from.toPK()
            ":user" from "USER-"
        }
    }.items().map { it["pk"] }.single()!!.s()

    val toUser = client.query("ledger") {
        useIndex("accounts")
        keyCondition("sk = :accountID and begins_with(pk, :user)")
        attributes {
            ":accountID" from transfer.to.toPK()
            ":user" from "USER-"
        }
    }.items().map { it["pk"] }.single()!!.s()

    client.writeTransaction {
        update("ledger") {
            key {
                "pk" from fromUser
                "sk" from transfer.from.toPK()

            }
            update("SET headroom = headroom - :a")

            condition("headroom >= :a")
            attributes {
                ":a" from transfer.amount
            }

        }
        update("ledger") {
            key {
                "pk" from toUser
                "sk" from transfer.to.toPK()

            }
            update("SET headroom = headroom + :a")
            attributes {
                ":a" from transfer.amount
            }

        }
        put("ledger") {
            item {
                "pk" from transfer.from.toPK()
                "sk" from "TX-$timeStamp-$id"
                "id" from id
                "type" from "DEBIT"
                "amount" from transfer.amount
                "description" from transfer.description
            }

        }
        put("ledger") {
            item {
                "pk" from transfer.to.toPK()
                "sk" from "TX-$timeStamp-$id"
                "id" from id
                "type" from "CREDIT"
                "amount" from transfer.amount
                "description" from transfer.description
            }
        }
    }

}