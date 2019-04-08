package com.ximedes.vas.domain

import com.ximedes.vas.dsl.take
import software.amazon.awssdk.services.dynamodb.model.AttributeValue

inline class AccountID(val id: String)

data class Account(
    val owner: UserID,
    val id: AccountID,
    val balance: Long,
    val overdraft: Long,
    val description: String
)

fun Map<String, AttributeValue>.asAccount(): Account {
    val userID = UserID(take("owner_id"))
    val accountID = AccountID(take("sk"))
    val overdraft = take<Long>("overdraft")
    val balance = take<Long>("headroom") - overdraft
    val description = take<String>("description")
    return Account(userID, accountID, balance, overdraft, description)
}