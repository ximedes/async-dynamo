package com.ximedes.vas.domain

inline class TransferID(val id: String)

data class Transfer(
    val id: TransferID,
    val from: AccountID,
    val to: AccountID,
    val amount: Long,
    val description: String
)