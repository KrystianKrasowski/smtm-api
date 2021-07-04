package com.smtm.security

import com.smtm.security.api.Token
import com.smtm.security.registration.*
import io.cucumber.java.DataTableType
import io.cucumber.java.ParameterType
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class ParameterTypes(private val world: World) {

    @ParameterType(".*")
    fun unencryptedPassword(input: String) = unencryptedPasswordOf(input)

    @ParameterType(".*")
    fun emailAddress(input: String) = emailAddressOf(input)

    @ParameterType("((\\d{4})-(\\d{2})-(\\d{2}) (\\d{2}):(\\d{2}):(\\d{2}))")
    fun instant(input: String): Instant = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        .let { LocalDateTime.parse(input, it) }
        .toInstant(ZoneOffset.UTC)

    @DataTableType
    fun validUserProfileEntry(entry: Map<String, String>): ValidUserProfileEntry {
        return ValidUserProfileEntry(
            id = entry.getValue("id").toLong(),
            emailAddress = entry.getValue("email").toEmailAddress(),
            password = entry.getValue("password").toUnencryptedPassword()
        )
    }

    @DataTableType
    fun token(entry: Map<String, String>): Token {
        return world.tokenFactory.create(
            subject = entry.getValue("sub").toLong(),
            expiresAt = instant(entry.getValue("exp"))
        )
    }

    @DataTableType
    fun tokenString(entry: Map<String, String>): String {
        return token(entry).toString()
    }
}

data class ValidUserProfileEntry(val id: Long, val emailAddress: EmailAddress, val password: UnencryptedPassword)
