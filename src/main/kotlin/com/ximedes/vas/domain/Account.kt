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
    val headroom: Long by item.prop("headroom")

    override fun toString(): String {
        return "AccountItem(owner=$ownerID, overdraft=$headroom)"
    }

}

