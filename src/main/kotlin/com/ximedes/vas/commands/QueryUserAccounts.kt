package com.ximedes.vas.commands

import com.ximedes.vas.Ledger
import com.ximedes.vas.domain.Account
import com.ximedes.vas.domain.UserID
import com.ximedes.vas.dsl.query

suspend fun Ledger.queryUserAccounts(userID: UserID): List<Account> {
    val result = client.query("ledger") {
        keyCondition("pk = :userId")
        attributes {
            ":userId" from userID.toPK()
        }
    }

    return result.items().map { it.accountFromDynamo() }

}
