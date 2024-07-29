package com.smtm.infrastructure.persistence

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.smtm.core.domain.OwnerId
import com.smtm.core.domain.categories.Categories
import com.smtm.core.domain.categories.CategoriesProblem
import com.smtm.core.spi.CategoriesRepository
import com.smtm.infrastructure.persistence.categories.CategoryInSetRecord
import com.smtm.infrastructure.persistence.categories.Conversions.toCategoryRecord
import com.smtm.infrastructure.persistence.categories.Conversions.toCategorySetRecord
import com.smtm.infrastructure.persistence.categories.Conversions.toDomain
import javax.sql.DataSource
import org.slf4j.LoggerFactory
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.transaction.support.TransactionTemplate

class CategoriesRepositoryJdbcAdapter(
    dataSource: DataSource
) : CategoriesRepository {

    private val jdbc = JdbcTemplate(dataSource)
    private val transactions = TransactionTemplate(DataSourceTransactionManager(dataSource))

    override fun getCategories(ownerId: OwnerId): Either<CategoriesProblem, Categories> =
        jdbc.runCatching { CategoryInSetRecord.getByOwnerId(ownerId, this) }
            .mapCatching { it.toDomain(ownerId) }
            .map { it.right() }
            .onFailure { logger.error("Cannot fetch categories", it) }
            .getOrElse { otherCategoriesProblemOf("Cannot fetch categories") }

    override fun save(categories: Categories): Either<CategoriesProblem, Categories> =
        categories
            .takeIf { it.isModificationNeeded() }
            ?.applyChanges()
            ?: categories.right()

    private fun Categories.isModificationNeeded() = toDelete.isNotEmpty() || toSave.isNotEmpty()

    private fun Categories.applyChanges() = transactions.execute { trn ->
        this.runCatching { incrementVersion() }
            .mapCatching { it.saveCategories() }
            .mapCatching { it.deleteAllMarked() }
            .onFailure { trn.setRollbackOnly() }
            .onFailure { logger.error("Cannot save categories", it) }
            .map { getCategories(id) }
            .getOrElse { otherCategoriesProblemOf("Cannot save categories") }
    }

    private fun Categories.incrementVersion() = apply {
        toCategorySetRecord(jdbc).upsert()
    }

    private fun Categories.saveCategories() = apply {
        toSave
            .map { it.toCategoryRecord(id, jdbc) }
            .forEach { it.upsert() }
    }

    private fun Categories.deleteAllMarked() = apply {
        toDelete
            .map { it.toCategoryRecord(id, jdbc) }
            .forEach { it.delete() }
    }

    private fun otherCategoriesProblemOf(message: String) =
        CategoriesProblem.Other(message).left()

}

private val logger = LoggerFactory.getLogger(CategoriesRepositoryJdbcAdapter::class.java)

