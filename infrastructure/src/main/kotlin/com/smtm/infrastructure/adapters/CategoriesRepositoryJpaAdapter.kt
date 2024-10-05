package com.smtm.infrastructure.adapters

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.smtm.core.domain.OwnerId
import com.smtm.core.domain.categories.Category
import com.smtm.core.domain.tags.Tags
import com.smtm.core.domain.tags.TagsProblem
import com.smtm.core.spi.CategoriesRepository
import com.smtm.infrastructure.persistence.categories.CategorySetEntity
import com.smtm.infrastructure.persistence.categories.CategorySetsJpaRepository
import jakarta.persistence.EntityManager
import org.slf4j.LoggerFactory
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory
import org.springframework.transaction.annotation.Transactional

open class CategoriesRepositoryJpaAdapter(
    entityManager: EntityManager,
    private val ownerIdProvider: () -> OwnerId,
) : CategoriesRepository {

    private val repository = JpaRepositoryFactory(entityManager).getRepository(CategorySetsJpaRepository::class.java)
    private val logger = LoggerFactory.getLogger(CategoriesRepositoryJpaAdapter::class.java)

    override fun getCategories(): Either<TagsProblem, Tags<Category>> =
        repository
            .runCatching { findByOwnerId(ownerIdProvider().asString()) }
            .map { it?.toDomain() ?: Tags.empty(ownerIdProvider().asEntityId()) }
            .map { it.right() }
            .onFailure { logger.error("Failed to fetch all categories", it) }
            .getOrElse { TagsProblem.from(it).left() }

    @Transactional
    override fun save(categories: Tags<Category>): Either<TagsProblem, Tags<Category>> =
        repository
            .runCatching { save(CategorySetEntity.from(categories)) }
            .map { it.toDomain() }
            .map { it.right() }
            .onFailure { logger.error("Failed to save categories", it) }
            .getOrElse { TagsProblem.from(it).left() }
}
