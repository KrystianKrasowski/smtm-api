package com.smtm.security.registration

private const val EmailPattern = "^[A-Za-zęóąśłżźćńĘÓĄŚŁŻŹĆŃ0-9._%+-]+@[A-ZęóąśłżźćńĘÓĄŚŁŻŹĆŃa-z0-9.-]+\\.[A-ZęóąśłżźćńĘÓĄŚŁŻŹĆŃa-z]{2,6}$"

data class EmailAddress internal constructor(val address: String) {

    private val notAnEmailViolation = Violation.NotAnEmailAddress
            .takeUnless { EmailPattern.toRegex().matches(address) }

    fun getViolations(): Collection<Violation> = listOfNotNull(notAnEmailViolation)

    override fun toString(): String {
        return address
    }
}

fun emailAddressOf(address: String) = EmailAddress(address)

fun String.toEmailAddress() = emailAddressOf(this)
