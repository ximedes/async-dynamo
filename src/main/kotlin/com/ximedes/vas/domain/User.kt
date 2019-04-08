package com.ximedes.vas.domain

inline class UserID(val id: String)

data class User(val id: UserID, val email: String)