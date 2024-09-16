package com.smtm.core.domain

@JvmInline
value class EntityId(private val value: String) {

    override fun toString(): String {
        return value
    }

    companion object {

        fun of(value: String): EntityId =
            EntityId(value)
    }
}
