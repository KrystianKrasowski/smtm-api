package com.smtm.application.domain

@JvmInline
value class Version(val number: Long) {

    fun increment(): Version = Version(number + 1)
}

fun versionOf(number: Long) = Version(number)