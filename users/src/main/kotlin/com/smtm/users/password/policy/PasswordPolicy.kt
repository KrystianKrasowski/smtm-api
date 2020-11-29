package com.smtm.users.password.policy

import com.smtm.users.registration.UnencryptedPassword
import com.smtm.users.registration.Violation

interface PasswordPolicy {

    fun validate(password: UnencryptedPassword): Collection<Violation>
}
