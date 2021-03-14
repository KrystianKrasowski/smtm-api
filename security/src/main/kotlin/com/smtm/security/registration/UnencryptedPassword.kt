package com.smtm.security.registration

import com.smtm.common.ConstraintViolation
import com.smtm.security.password.policy.PasswordPolicy

data class UnencryptedPassword internal constructor(private val value: String) {

    fun getViolations(policy: PasswordPolicy): Collection<ConstraintViolation> = policy.validate(this)

    override fun toString() = value
}

fun unencryptedPasswordOf(value: String?) = UnencryptedPassword(value ?: "")

fun String.toUnencryptedPassword() = unencryptedPasswordOf(this)
