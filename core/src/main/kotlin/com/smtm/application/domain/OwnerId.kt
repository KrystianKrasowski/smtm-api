package com.smtm.application.domain

@JvmInline
value class OwnerId(val value: Long)

fun ownerIdOf(id: Long) = OwnerId(id)