package com.smtm.application.security

import com.smtm.security.api.Token

class FakeToken(override val userId: Long, private val token: String) : Token {

    override fun toString(): String {
        return token
    }
}

fun tokenOf(value: String) = FakeToken(1, value)
