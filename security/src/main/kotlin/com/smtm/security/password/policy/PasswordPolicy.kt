package com.smtm.security.password.policy

import com.smtm.security.registration.UnencryptedPassword
import com.smtm.security.registration.Violation

interface PasswordPolicy {

    fun validate(password: UnencryptedPassword): Collection<Violation>
}
