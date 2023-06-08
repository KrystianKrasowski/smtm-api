package com.smtm.application.domain

@JvmInline
value class Version(val value: Long) {

    fun increment(): Version = Version(value + 1)
}

fun versionOf(number: Long) = Version(number)