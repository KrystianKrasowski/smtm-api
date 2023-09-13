package com.smtm.application.domain

@JvmInline
value class Version(val value: Int) {

    fun increment(): Version = Version(value + 1)

    companion object {

        val ZERO = Version(0)
    }
}

fun versionOf(number: Long) = Version(number.toInt())
fun versionOf(number: Int) = versionOf(number.toLong())

fun Int.toVersion() = Version(this)
