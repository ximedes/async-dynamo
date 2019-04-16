package com.ximedes.vas.api

data class CreateUserRequest(val id: String, val email: String)
data class CreateAccountRequest(val account: String, val user: String, val overdraft: Long, val description: String)
data class TransferRequest(val from: String, val to: String, val amount: Long, val description: String)