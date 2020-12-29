package com.smtm.application.security.v1

import com.smtm.security.token.Token

data class TokenDto(val value: String) {

    companion object {

        const val MediaTypeValue = "application/smtm.token.v1+json"
    }
}

fun tokenDtoOf(token: Token) = tokenDtoOf(token.toString())

fun tokenDtoOf(token: String) = TokenDto(token)
