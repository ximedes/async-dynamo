package com.ximedes.vas.domain

private val userIDRegex = Regex("""USER-(.*)""")

inline class UserID(private val id: String) {
    companion object {
        fun fromDynamo(s: String) = UserID(userIDRegex.matchEntire(s)!!.groupValues.last())
    }

    fun toPK() = "USER-$id"
}