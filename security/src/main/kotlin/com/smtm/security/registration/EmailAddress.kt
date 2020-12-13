package com.smtm.security.registration

private const val EmailPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"

data class EmailAddress internal constructor(val address: String) {

    private val notAnEmailViolation = Violation.NotAnEmailAddress
            .takeUnless { EmailPattern.toRegex().matches(address) }

    fun getViolations(): Collection<Violation> = listOfNotNull(notAnEmailViolation)

    override fun toString(): String {
        return address
    }
}

fun emailAddressOf(address: String) = EmailAddress(address)
