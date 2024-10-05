package com.smtm.core.domain.tags

import arrow.core.Either
import arrow.core.NonEmptySet
import arrow.core.left
import arrow.core.right
import arrow.core.toNonEmptySetOrNull
import com.smtm.core.domain.Violation
import com.smtm.core.domain.shared.extractIllegalCharactersFrom

internal class TagValidator<T: Tag>(
    private val tag: T,
    private val tags: Collection<T>
) {

    private val violations = mutableSetOf<Violation>()

    fun validate(block: TagValidator<T>.() -> Unit): Either<NonEmptySet<Violation>, T> =
        apply(block)
            .let { violations.toNonEmptySetOrNull() }
            ?.left()
            ?: tag.right()

    fun hasUniqueName(): Violation? =
        Violation.nonUnique("name")
            .takeIf { tags.any { it.id != tag.id && it.name == tag.name } }
            ?.also { violations.add(it) }

    fun hasNotEmptyName(): Violation? =
        Violation.empty("name")
            .takeIf { tag.name.isBlank() }
            ?.also { violations.add(it) }

    fun hasLegalCharacters(): Violation? =
        "[\\p{IsLatin}0-9 ]+"
            .toRegex()
            .extractIllegalCharactersFrom(tag.name)
            .takeIf { it.isNotEmpty() }
            ?.let { Violation.illegalCharacters("name", it) }
            ?.also { violations.add(it) }
}
