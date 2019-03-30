package com.ximedes.vas.domain

import com.ximedes.vas.dsl.take
import software.amazon.awssdk.services.dynamodb.model.AttributeValue

private val accountIDRegex = Regex("""ACC-(.*)""")

inline class AccountID(private val id: String) {
    companion object {
        fun fromDynamo(s: String) = AccountID(accountIDRegex.matchEntire(s)!!.groupValues.last())
    }

    fun toPK() = "ACC-$id"
}

data class Account(
    val owner: UserID,
    val id: AccountID,
    val balance: Long,
    val overdraft: Long,
    val description: String
)

fun Map<String, AttributeValue>.asAccount(): Account {
    val userID = UserID.fromDynamo(take("pk"))
    val accountID = AccountID.fromDynamo(take("sk"))
    val overdraft = take<Long>("overdraft")
    val balance = take<Long>("headroom") - overdraft
    val description = take<String>("description")
    return Account(userID, accountID, balance, overdraft, description)
}