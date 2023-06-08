package com.smtm.application.domain

@JvmInline
value class UserId(val value: Long)

fun userIdOf(id: Long) = UserId(id)