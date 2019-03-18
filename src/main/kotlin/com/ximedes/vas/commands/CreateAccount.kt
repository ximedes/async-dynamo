package com.ximedes.vas.commands

import com.ximedes.vas.Ledger
import com.ximedes.vas.NewAccountMessage
import com.ximedes.vas.dsl.writeTransaction

suspend fun Ledger.createAccount(msg: NewAccountMessage) {
    client.writeTransaction {
        put("ledger") {
            item {
                "pk" from "USER-${msg.user}"
                "sk" from "ACC-${msg.account}"
                "overdraft" from msg.overdraft
                "headroom" from msg.overdraft
                "description" from msg.description
            }
            condition("attribute_not_exists(sk)")
        }
    }
}