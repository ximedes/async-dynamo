package com.ximedes.vas.domain

import com.ximedes.vas.dsl.Item
import com.ximedes.vas.dsl.prop

inline class AccountID(val id: String)

data class Account(
    val owner: UserID,
    val id: AccountID,
    val balance: Long,
    val overdraft: Long,
    val description: String
)

class AccountItem(item: Item) {
    val ownerID: String by item.prop("owner_id")
    val accountID: String by item.prop("pk")
    val headroom: Long by item.prop("headroom")
    val overdraft: Long by item.prop("overdraft")
    val description: String by item.prop("description")
}

