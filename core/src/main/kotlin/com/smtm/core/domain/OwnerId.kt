package com.smtm.core.domain

@JvmInline
value class OwnerId(private val value: String) {

    override fun toString(): String =
        asString()

    fun asString(): String =
        value

    companion object {

        fun of(id: String): OwnerId =
            OwnerId(id)
    }
}
