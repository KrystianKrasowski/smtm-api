package com.smtm.users.spi

import com.smtm.users.registration.Password
import com.smtm.users.registration.passwordOf

class FakePasswordEncryption : PasswordEncryption {

    override fun encrypt(value: String): Password {
        return passwordOf(value)
    }
}
