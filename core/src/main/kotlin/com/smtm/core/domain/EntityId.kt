package com.smtm.core.domain

import java.util.UUID

@JvmInline
value class EntityId(private val value: String) {

    override fun toString(): String {
        return value
    }

    companion object {

        fun of(value: String): EntityId =
            EntityId(value)

        fun generate(prefix: String): EntityId =
            of("$prefix-${UUID.randomUUID()}")
    }
}
