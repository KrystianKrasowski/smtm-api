package com.smtm.application.domain

@JvmInline
value class Version(val value: Long) {

    fun increment(): Version = Version(value + 1)

    fun isFirst(): Boolean = value == 1L

    companion object {

        val ZERO = Version(0)
    }
}

fun versionOf(number: Long) = Version(number)
fun versionOf(number: Int) = versionOf(number.toLong())