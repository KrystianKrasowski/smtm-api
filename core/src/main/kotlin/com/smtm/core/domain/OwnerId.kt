package com.smtm.core.domain

@JvmInline
value class OwnerId(val value: String) {

    companion object {

        fun of(id: String): OwnerId =
            OwnerId(id)
    }
}
