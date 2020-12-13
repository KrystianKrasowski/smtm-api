package com.smtm.security.registration

import com.smtm.security.password.policy.PasswordPolicy
import com.smtm.security.spi.PasswordEncryption

data class UnencryptedPassword internal constructor(private val value: String) {

    fun encrypt(passwordEncryption: PasswordEncryption): Password = passwordEncryption.encrypt(value)

    fun getViolations(policy: PasswordPolicy): Collection<Violation> = policy.validate(this)

    override fun toString() = value
}

data class Password(val value: String)

fun unencryptedPasswordOf(value: String?) = UnencryptedPassword(value ?: "")

fun passwordOf(value: String) = Password(value)
