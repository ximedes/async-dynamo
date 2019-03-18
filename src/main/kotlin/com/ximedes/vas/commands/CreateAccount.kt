package com.ximedes.vas.commands

import com.ximedes.vas.Ledger
import com.ximedes.vas.domain.Account
import com.ximedes.vas.domain.UserID
import com.ximedes.vas.domain.extractAccountID
import com.ximedes.vas.dsl.writeTransaction
import software.amazon.awssdk.services.dynamodb.model.AttributeValue

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

fun Map<String, AttributeValue>.accountFromDynamo(): Account {
    val userID = UserID(get("pk")!!.s())
    val accountID = get("sk")!!.s().extractAccountID()
    val overdraft = get("overdraft")!!.n().toLong()
    val balance = get("headroom")!!.n().toLong() - overdraft
    val description = get("description")!!.s()
    return Account(userID, accountID, balance, overdraft, description)
}