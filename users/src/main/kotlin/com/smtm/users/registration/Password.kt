package com.smtm.users.registration

import com.smtm.users.spi.PasswordEncryption

data class UnsecuredPassword(private val value: String) {

    fun encrypt(passwordEncryption: PasswordEncryption): Password = passwordEncryption.encrypt(value)

    fun isSecure() = hasAtLeastOneUppercaseLetter() && hasAtLeastOneSpecialChar() && hasAtLeastOneDigit()

    private fun hasAtLeastOneUppercaseLetter() = "[A-Z]+"
            .toRegex()
            .containsMatchIn(value)

    private fun hasAtLeastOneSpecialChar() = "[~!@#$%&*()_+=-]+"
            .toRegex()
            .containsMatchIn(value)

    private fun hasAtLeastOneDigit() = "[0-9]+"
            .toRegex()
            .containsMatchIn(value)
}

data class Password(val value: String)

fun unsecuredPasswordOf(value: String) = UnsecuredPassword(value)

fun passwordOf(value: String) = Password(value)
