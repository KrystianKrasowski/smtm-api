package com.smtm.core.domain

@JvmInline
value class OwnerId(val value: Long) {

    companion object {

        fun of(id: Long): OwnerId =
            OwnerId(id)
    }
}
