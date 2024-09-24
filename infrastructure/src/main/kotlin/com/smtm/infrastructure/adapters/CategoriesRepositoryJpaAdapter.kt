package com.smtm.infrastructure.adapters

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.smtm.core.domain.OwnerId
import com.smtm.core.domain.categories.Categories
import com.smtm.core.domain.categories.CategoriesProblem
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

    override fun getCategories(): Either<CategoriesProblem, Categories> =
        repository
            .runCatching { findByOwnerId(ownerIdProvider().asString()) }
            .map { it?.toDomain() ?: Categories.empty(ownerIdProvider()) }
            .map { it.right() }
            .onFailure { logger.error("Failed to fetch all categories", it) }
            .getOrElse { CategoriesProblem.failure(it).left() }

    @Transactional
    override fun save(categories: Categories): Either<CategoriesProblem, Categories> =
        repository
            .runCatching { save(CategorySetEntity.from(categories)) }
            .map { it.toDomain() }
            .map { it.right() }
            .onFailure { logger.error("Failed to save categories", it) }
            .getOrElse { CategoriesProblem.failure(it).left() }
}
