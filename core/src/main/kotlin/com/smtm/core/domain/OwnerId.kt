package com.smtm.core.domain

@JvmInline
value class OwnerId(val value: Long)

fun ownerIdOf(id: Long) = OwnerId(id)

fun Long.toOwnerId() = OwnerId(this)

fun Int.toOwnerId() = OwnerId(this.toLong())
