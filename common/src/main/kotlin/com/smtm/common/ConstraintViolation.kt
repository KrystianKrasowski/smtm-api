package com.smtm.common

data class ConstraintViolation internal constructor(val property: String, val message: Message)

data class Message internal constructor(val pattern: String, val parameters: Map<String, String>)

fun constraintViolationOf(property: String, message: Message) = ConstraintViolation(property, message)

fun messageOf(pattern: String, parameters: Map<String, String> = emptyMap()) = Message(pattern, parameters)
