package com.smtm.application.security.v1

data class TokensDto(val accessToken: String, val refreshToken: String) {

    companion object {

        const val MediaTypeValue = "application/smtm.tokens.v1+json"
    }
}

fun tokensDtoOf(accessToken: String, refreshToken: String) = TokensDto(accessToken, refreshToken)
