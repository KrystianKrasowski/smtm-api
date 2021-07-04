package com.smtm.application.security.v1

data class RefreshTokenDto(val token: String) {

    companion object {

        const val MediaTypeValue = "application/smtm.refresh-token.v1+json"
    }
}
