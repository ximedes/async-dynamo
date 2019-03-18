package com.ximedes.vas.domain

private val userIDRegex = Regex("""USER-(.*)""")

fun String.extractUserID() = UserID(userIDRegex.matchEntire(this)!!.groupValues.last())

inline class UserID(val id: String) {
    fun toPK() = "USER-${id}"
}