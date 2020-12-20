package com.smtm.security.registration

import com.smtm.security.password.policy.PasswordPolicy

data class UnencryptedPassword internal constructor(private val value: String) {

    fun getViolations(policy: PasswordPolicy): Collection<Violation> = policy.validate(this)

    override fun toString() = value
}

fun unencryptedPasswordOf(value: String?) = UnencryptedPassword(value ?: "")

fun String.toUnencryptedPassword() = unencryptedPasswordOf(this)
