package com.smtm.users.spi

import com.smtm.users.registration.Password
import com.smtm.users.registration.passwordOf

class FakePasswordEncryption : PasswordEncryption {

    private val encryptionMap = mutableMapOf<String, Password>()

    override fun encrypt(value: String): Password = encryptionMap[value] ?: passwordOf(value)

    fun putEncryption(key: String, value: String) = apply { encryptionMap[key] = passwordOf(value) }
}
