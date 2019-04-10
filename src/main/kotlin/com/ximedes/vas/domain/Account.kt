package com.ximedes.vas.domain

import com.ximedes.vas.dsl.Item
import com.ximedes.vas.dsl.take

inline class AccountID(val id: String)

data class Account(
    val owner: UserID,
    val id: AccountID,
    val balance: Long,
    val overdraft: Long,
    val description: String

)

fun Item.asAccount(): Account {
    val userID = UserID(take("owner_id"))
    val accountID = AccountID(take("pk"))
    val overdraft = take<Long>("overdraft")
    val balance = take<Long>("headroom") - overdraft
    val description = take<String>("description")
    return Account(userID, accountID, balance, overdraft, description)
}