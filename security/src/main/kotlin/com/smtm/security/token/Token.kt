package com.smtm.security.token

data class Token internal constructor(val value: String)

fun tokenOf(value: String) = Token(value)
