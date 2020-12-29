package com.smtm.security.token

data class Token internal constructor(val value: String, val userId: Long) {

    override fun toString() = value
}

fun tokenOf(token: String, userId: Long) = Token(token, userId)
