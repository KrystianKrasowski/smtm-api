package com.smtm.security.password.policy

import com.smtm.common.ConstraintViolation
import com.smtm.security.registration.UnencryptedPassword

interface PasswordPolicy {

    fun validate(password: UnencryptedPassword): Collection<ConstraintViolation>
}
