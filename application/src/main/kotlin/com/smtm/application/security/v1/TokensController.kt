package com.smtm.application.security.v1

import com.smtm.security.api.CredentialsAuthentication
import com.smtm.security.api.RefreshTokenAuthentication
import com.smtm.security.authentication.Tokens
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/security/tokens"])
class TokensController(private val credentialsAuthentication: CredentialsAuthentication,
                       private val refreshTokenAuthentication: RefreshTokenAuthentication) {

    @PostMapping(
        consumes = [CredentialsDto.MediaTypeValue],
        produces = [TokensDto.MediaTypeValue]
    )
    fun createToken(@RequestBody credentials: CredentialsDto): ResponseEntity<*> = credentials
        .authenticate()
        ?.toResponseEntity()
        ?: unauthorized

    @PostMapping(
        consumes = [RefreshTokenDto.MediaTypeValue],
        produces = [TokensDto.MediaTypeValue]
    )
    fun createTokens(@RequestBody refreshToken: RefreshTokenDto): ResponseEntity<*> = refreshToken
        .authenticate()
        ?.toResponseEntity()
        ?: unauthorized

    private fun CredentialsDto.authenticate() = credentialsAuthentication.authenticate(email, password)

    private fun RefreshTokenDto.authenticate() = refreshTokenAuthentication.authenticate(token)
}

private fun Tokens.toResponseEntity() = tokensDtoOf(accessToken.value, refreshToken.value)
    .let { ResponseEntity.ok(it) }

private val unauthorized = ResponseEntity.status(401).build<Nothing>()
