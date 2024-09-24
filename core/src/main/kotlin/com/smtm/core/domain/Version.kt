package com.smtm.core.domain

@JvmInline
value class Version(private val value: Int) {

    fun asInt(): Int =
        value

    companion object {

        fun of(value: Int): Version =
            Version(value)
    }
}
