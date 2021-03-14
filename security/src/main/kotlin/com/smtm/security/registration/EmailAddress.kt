package com.smtm.security.registration

data class EmailAddress internal constructor(val address: String) {

    override fun toString(): String {
        return address
    }
}

fun emailAddressOf(address: String) = EmailAddress(address)

fun String.toEmailAddress() = emailAddressOf(this)
