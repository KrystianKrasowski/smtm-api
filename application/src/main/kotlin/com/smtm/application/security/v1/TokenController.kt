package com.smtm.application.security.v1

import com.smtm.security.api.Authentication
import com.smtm.security.token.Token
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/security/token"])
class TokenController(private val authentication: Authentication) {

    @PostMapping(
        consumes = [CredentialsDto.MediaTypeValue],
        produces = [TokenDto.MediaTypeValue]
    )
    fun createToken(@RequestBody credentials: CredentialsDto): ResponseEntity<*> = credentials
        .authenticate()
        ?.toResponseEntity()
        ?: unauthorized

    private fun CredentialsDto.authenticate() = authentication.authenticate(email, password)

}

private fun Token.toResponseEntity() = tokenDtoOf(this)
    .let { ResponseEntity.ok(it) }

private val unauthorized = ResponseEntity.status(401).build<Nothing>()
