package com.smtm.core.domain

@JvmInline
value class Version(val value: Int) {

    fun increment(): Version =
        Version(value = value + 1)

    companion object {

        fun of(value: Int): Version =
            Version(value)
    }
}
