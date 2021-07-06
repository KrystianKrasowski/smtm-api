package com.smtm.security

import com.smtm.security.api.AccessToken
import com.smtm.security.api.RefreshToken
import com.smtm.security.registration.*
import com.smtm.security.spi.FakeRefreshTokensRepository
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
    fun accessToken(entry: Map<String, String>): AccessToken {
        return world.tokenFactory.createAccessToken(
            subject = entry.getValue("subject").toLong(),
            expiresAt = instant(entry.getValue("expires at"))
        )
    }

    @DataTableType
    fun refreshToken(entry: Map<String, String>): RefreshToken {
        return world.tokenFactory.createRefreshToken(
            subject = entry.getValue("subject").toLong(),
            expiresAt = instant(entry.getValue("expires at")),
            id = entry.getValue("id")
        )
    }

    @DataTableType
    fun fakeRefreshTokenRepositoryRecord(entry: Map<String, String>): FakeRefreshTokensRepository.Record {
        return FakeRefreshTokensRepository.Record(
            subject = entry.getValue("subject").toLong(),
            id = entry.getValue("id")
        )
    }
}

data class ValidUserProfileEntry(val id: Long, val emailAddress: EmailAddress, val password: UnencryptedPassword)
