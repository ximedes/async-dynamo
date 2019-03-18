package com.ximedes.vas.domain

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