package com.smtm.core.domain

@JvmInline
value class Version(val value: Int) {

    companion object {

        fun of(value: Int): Version =
            Version(value)
    }
}
