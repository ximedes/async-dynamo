package com.ximedes.vas.domain

data class Transfer(
    val from: AccountID,
    val to: AccountID,
    val amount: Long,
    val description: String
)