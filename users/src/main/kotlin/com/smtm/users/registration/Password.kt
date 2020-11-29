package com.smtm.users.registration

import com.smtm.users.password.policy.PasswordPolicy
import com.smtm.users.spi.PasswordEncryption

data class UnencryptedPassword internal constructor(private val value: String) {

    fun encrypt(passwordEncryption: PasswordEncryption): Password = passwordEncryption.encrypt(value)

    fun getViolations(policy: PasswordPolicy) = policy.validate(this)

    override fun toString() = value
}

data class Password(val value: String)

fun unencryptedPasswordOf(value: String?) = UnencryptedPassword(value ?: "")

fun passwordOf(value: String) = Password(value)
