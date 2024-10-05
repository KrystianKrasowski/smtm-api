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

    fun getBy(id: EntityId): Either<TagsProblem, T> =
        getBy { it.id == id }

    fun getBy(name: String): Either<TagsProblem, T> =
        getBy { it.name == name }

    fun add(tag: T): Either<TagsProblem, Tags<T>> =
        tag.validate()
            .map { it.upsert() }

    fun replace(tag: T): Either<TagsProblem, Tags<T>> =
        tag.takeIf { hasTagWith(tag.id) }
            ?.validate()
            ?.map { it.upsert() }
            ?: TagsProblem.unknown(tag.id).left()

    fun delete(id: EntityId): Either<TagsProblem, Tags<T>> =
        id.takeIf { hasTagWith(it) }
            ?.let { deleteTagById(it) }
            ?.right()
            ?: TagsProblem.unknown(id).left()

    private fun getBy(predicate: (T) -> Boolean): Either<TagsProblem, T> =
        tagsCollection
            .runCatching { first(predicate) }
            .map { it.right() }
            .getOrElse { TagsProblem.from(it).left() }

    private fun T.validate(): Either<TagsProblem, T> =
        TagValidator(this, tagsCollection)
            .validate {
                hasUniqueName()
                hasNotEmptyName()
                hasLegalCharacters()
            }
            .mapLeft { TagsProblem.from(it) }

    private  fun T.upsert(): Tags<T> =
        tagsCollection.toMutableList()
            .apply { removeIf { it.id == id } }
            .also { it.add(this) }
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
