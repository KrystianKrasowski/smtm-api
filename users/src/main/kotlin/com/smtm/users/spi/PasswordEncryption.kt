package com.smtm.users.spi

import com.smtm.users.registration.Password

interface PasswordEncryption {

    fun encrypt(value: String): Password
}
