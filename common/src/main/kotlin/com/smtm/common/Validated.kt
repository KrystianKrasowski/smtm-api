package com.smtm.common

sealed class Validated<T>(private val subject: T?, private val violations: List<ConstraintViolation>) {

    class Success<T> internal constructor(subject: T) : Validated<T>(subject, emptyList())
    class Failure<T> internal constructor(violations: List<ConstraintViolation>) : Validated<T>(null, violations)

    @Suppress("UNCHECKED_CAST")
    fun <R> map(transform: (T) -> R): Validated<R> = subject
        ?.let(transform)
        ?.let { validationSuccessOf(it) }
        ?: this as Validated<R>

    fun orElse(transform: (List<ConstraintViolation>) -> T) = subject ?: transform(violations)
}

fun <T> validationSuccessOf(subject: T): Validated<T> = Validated.Success(subject)

fun <T> validationFailureOf(violations: List<ConstraintViolation>): Validated<T> = Validated.Failure(violations)
