package com.smtm.core.domain.tags

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.smtm.core.domain.EntityId
import com.smtm.core.domain.Version

data class Tags<T : Tag>(
    val id: EntityId,
    val version: Version,
    private val tagsCollection: Collection<T>
) : Iterable<T> {

    override fun iterator(): Iterator<T> =
        tagsCollection.iterator()

    fun getById(id: EntityId): Either<TagsProblem, T> =
        tagsCollection
            .runCatching { first { it.id == id } }
            .map { it.right() }
            .getOrElse { TagsProblem.from(it).left() }

    fun getByName(name: String): Either<TagsProblem, T> =
        tagsCollection
            .runCatching { first { it.name == name } }
            .map { it.right() }
            .getOrElse { TagsProblem.from(it).left() }

    fun add(tag: T): Either<TagsProblem, Tags<T>> =
        validate(tag)
            .map { upsertTag(it) }

    fun replace(tag: T): Either<TagsProblem, Tags<T>> =
        tag.takeIf { hasTagWith(tag.id) }
            ?.let { validate(it) }
            ?.map { upsertTag(it) }
            ?: TagsProblem.unknown(tag.id).left()

    fun delete(id: EntityId): Either<TagsProblem, Tags<T>> =
        id.takeIf { hasTagWith(it) }
            ?.let { deleteTagById(it) }
            ?.right()
            ?: TagsProblem.unknown(id).left()

    private fun validate(tag: T): Either<TagsProblem, T> =
        TagValidator(tag, tagsCollection)
            .validate {
                hasUniqueName()
                hasNotEmptyName()
                hasLegalCharacters()
            }
            .mapLeft { TagsProblem.from(it) }

    private fun upsertTag(tag: T): Tags<T> =
        tagsCollection.toMutableList()
            .apply { removeIf { it.id == tag.id } }
            .apply { add(tag) }
            .let { copy(tagsCollection = it.toList()) }

    private fun hasTagWith(id: EntityId): Boolean =
        tagsCollection.any { it.id == id }

    private fun deleteTagById(tagId: EntityId): Tags<T> =
        tagsCollection.toMutableList()
            .apply { removeIf { it.id == tagId } }
            .let { copy(tagsCollection = it.toList()) }

    companion object {

        fun <T : Tag> empty(id: EntityId): Tags<T> =
            Tags(id, Version.of(0), emptyList())
    }
}
