package com.ximedes.vas.domain

private val accountIDRegex = Regex("""ACC-(.*)""")

fun String.extractAccountID() = AccountID(accountIDRegex.matchEntire(this)!!.groupValues.last())

inline class AccountID(val id: String) {
    fun toPK() = "ACC-${id}"
}

data class Account(
    val owner: UserID,
    val id: AccountID,
    val balance: Long,
    val overdraft: Long,
    val description: String
)