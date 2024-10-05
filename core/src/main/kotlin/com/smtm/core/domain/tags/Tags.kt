package com.smtm.core.domain.tags

import arrow.core.Either
import arrow.core.NonEmptySet
import arrow.core.left
import arrow.core.right
import arrow.core.toNonEmptySetOrNull
import com.smtm.core.domain.EntityId
import com.smtm.core.domain.Version
import com.smtm.core.domain.Violation
import com.smtm.core.domain.shared.extractIllegalCharactersFrom

data class Tags(
    val id: EntityId,
    val version: Version,
    private val tagsCollection: Collection<Tag>,
) : Iterable<Tag> {

    override fun iterator(): Iterator<Tag> =
        tagsCollection.iterator()

    fun getByName(name: String): Tag =
        first { it.name == name }

    fun add(tag: Tag): Either<TagsProblem, Tags> =
        TagValidator(tag, tagsCollection)
            .validate {
                hasUniqueName()
                hasNotEmptyName()
                hasLegalCharacters()
            }
            .mapLeft { TagsProblem.from(it) }
            .map { upsertTag(it) }

    fun replace(tag: Tag): Either<TagsProblem, Tags> =
        tag.takeIf { hasTagWith(it.id) }
            ?.let { TagValidator(it, tagsCollection) }
            ?.validate {
                hasNotEmptyName()
                hasLegalCharacters()
            }
            ?.mapLeft { TagsProblem.from(it) }
            ?.map { upsertTag(it) }
            ?: TagsProblem.unknown(tag.id).left()

    fun delete(id: EntityId): Either<TagsProblem, Tags> =
        id.takeIf { hasTagWith(it) }
            ?.let { deleteTagBy(it) }
            ?.right()
            ?: TagsProblem.unknown(id).left()

    private fun upsertTag(tag: Tag): Tags =
        tagsCollection.toMutableList()
            .apply { removeIf { it.id == tag.id } }
            .apply { add(tag) }
            .let { copy(tagsCollection = it.toList()) }

    private fun hasTagWith(id: EntityId): Boolean =
        any { it.id == id }

    private fun deleteTagBy(id: EntityId): Tags =
        tagsCollection.toMutableList()
            .apply { removeIf { it.id == id } }
            .let { copy(tagsCollection = it.toList()) }
}

private class TagValidator(
    private val tag: Tag,
    private val tags: Collection<Tag>
) {

    private val violations = mutableSetOf<Violation>()

    fun validate(block: TagValidator.() -> Unit): Either<NonEmptySet<Violation>, Tag> =
        apply(block)
            .let { violations.toNonEmptySetOrNull() }
            ?.left()
            ?: tag.right()

    fun hasUniqueName(): Violation? =
        Violation.nonUnique("name")
            .takeIf { tags.any { it.name == tag.name } }
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
