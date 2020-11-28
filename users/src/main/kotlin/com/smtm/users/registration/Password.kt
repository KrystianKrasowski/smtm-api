package com.smtm.users.registration

import com.smtm.users.spi.PasswordEncryption

data class UnsecuredPassword(private val value: String) {

    fun encrypt(passwordEncryption: PasswordEncryption): Password = passwordEncryption.encrypt(value)
}

data class Password(val value: String)

fun unsecuredPasswordOf(value: String) = UnsecuredPassword(value)

fun passwordOf(value: String) = Password(value)
