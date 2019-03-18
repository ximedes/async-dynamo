package com.ximedes.vas.commands

import com.ximedes.vas.Ledger
import com.ximedes.vas.domain.Account
import com.ximedes.vas.dsl.writeTransaction

suspend fun Ledger.createAccount(account: Account) {
    client.writeTransaction {
        put("ledger") {
            item {
                "pk" from account.owner.toPK()
                "sk" from account.id.toPK()
                "overdraft" from account.overdraft
                "headroom" from account.overdraft
                "description" from account.description
            }
            condition("attribute_not_exists(sk)")
        }
    }
}