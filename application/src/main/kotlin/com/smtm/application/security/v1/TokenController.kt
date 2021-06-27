package com.smtm.application.security.v1

import com.smtm.security.api.Authentication
import com.smtm.security.authentication.Tokens
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/security/tokens"])
class TokenController(private val authentication: Authentication) {

    @PostMapping(
        consumes = [CredentialsDto.MediaTypeValue],
        produces = [TokensDto.MediaTypeValue]
    )
    fun createToken(@RequestBody credentials: CredentialsDto): ResponseEntity<*> = credentials
        .authenticate()
        ?.toResponseEntity()
        ?: unauthorized

    private fun CredentialsDto.authenticate() = authentication.authenticate(email, password)

}

private fun Tokens.toResponseEntity() = tokensDtoOf(accessToken.value, refreshToken.value)
    .let { ResponseEntity.ok(it) }

private val unauthorized = ResponseEntity.status(401).build<Nothing>()
