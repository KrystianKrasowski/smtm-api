package com.smtm.application.domain

@JvmInline
value class UserId(val id: Long)

fun userIdOf(id: Long) = UserId(id)