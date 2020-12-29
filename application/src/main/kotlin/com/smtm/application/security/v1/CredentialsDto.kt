package com.smtm.application.security.v1

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.smtm.security.registration.EmailAddress
import com.smtm.security.registration.UnencryptedPassword
import com.smtm.security.registration.emailAddressOf
import com.smtm.security.registration.unencryptedPasswordOf

@JsonDeserialize(using = CredentialsDtoDeserializer::class)
data class CredentialsDto(val email: EmailAddress, val password: UnencryptedPassword) {

    companion object {

        const val MediaTypeValue = "application/smtm.credentials.v1+json"
    }
}

private class CredentialsDtoDeserializer : JsonDeserializer<CredentialsDto>() {

    override fun deserialize(parser: JsonParser, context: DeserializationContext): CredentialsDto {
        val node = parser.codec.readTree<JsonNode>(parser)
        val email = node.get("email").asText()
        val password = node.get("password").asText()
        return credentialsDtoOf(email, password)
    }
}

fun credentialsDtoOf(emailAddress: String, password: String) = credentialsDtoOf(emailAddressOf(emailAddress), unencryptedPasswordOf(password))

fun credentialsDtoOf(emailAddress: EmailAddress, password: UnencryptedPassword) = CredentialsDto(emailAddress, password)
