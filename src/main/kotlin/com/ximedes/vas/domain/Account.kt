package com.ximedes.vas.domain

inline class AccountID(val id: String)

data class Account(
    val owner: UserID,
    val accountID: AccountID,
    val balance: Long,
    val overdraft: Long,
    val description: String
)

