package com.smtm.security.spi

import com.smtm.security.registration.Password

interface PasswordEncryption {

    fun encrypt(value: String): Password
}
