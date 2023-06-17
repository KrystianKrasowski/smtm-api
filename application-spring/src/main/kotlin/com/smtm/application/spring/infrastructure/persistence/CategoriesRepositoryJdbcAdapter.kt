package com.smtm.application.spring.infrastructure.persistence

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.smtm.application.domain.Icon
import com.smtm.application.domain.OwnerId
import com.smtm.application.domain.Version
import com.smtm.application.domain.categories.Categories
import com.smtm.application.domain.categories.CategoriesProblem
import com.smtm.application.domain.categories.Category
import com.smtm.application.domain.categories.categoryOf
import com.smtm.application.domain.ownerIdOf
import com.smtm.application.domain.versionOf
import com.smtm.application.repository.CategoriesRepository
import org.slf4j.LoggerFactory
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.jdbc.core.RowMapper
import org.springframework.transaction.support.TransactionOperations
import java.sql.ResultSet

private val logger = LoggerFactory.getLogger(CategoriesRepositoryJdbcAdapter::class.java)

class CategoriesRepositoryJdbcAdapter(
    private val jdbc: JdbcOperations,
    private val transactions: TransactionOperations
) : CategoriesRepository {

    override fun getCategories(ownerId: OwnerId): Either<CategoriesProblem, Categories> {
        return "SELECT C.ID, C.NAME, C.ICON, C.OWNER_ID, CS.VERSION FROM CATEGORIES C JOIN CATEGORY_SETS CS ON C.OWNER_ID = CS.OWNER_ID WHERE C.OWNER_ID = ?"
            .runCatching { jdbc.query(this, CategorySetEntryEntityMapper(), ownerId.value) }
            .mapCatching { it.toCategories(ownerId) }
            .map { it.right() }
            .onFailure { logger.error("Cannot fetch categories", it) }
            .getOrElse { otherCategoriesProblemOf("Cannot fetch categories") }
    }

    override fun save(categories: Categories): Either<CategoriesProblem, Categories> = transactions.execute { trn ->
        categories
            .runCatching { incrementVersion(this) }
            .mapCatching { insert(it) }
            .onFailure { trn.setRollbackOnly() }
            .onFailure { logger.error("Cannot save categories", it) }
            .map { getCategories(categories.id) }
            .getOrElse { otherCategoriesProblemOf("Cannot save categories") }
    }!!

    private fun List<CategorySetEntryEntity>.toCategories(ownerId: OwnerId) = Categories(
        id = firstOrNull()?.ownerId ?: ownerId,
        version = firstOrNull()?.setVersion ?: versionOf(0),
        list = map { categoryOf(it.id, it.name, it.icon) }
    )

    private fun otherCategoriesProblemOf(message: String) = CategoriesProblem.Other(message).left()

    private fun incrementVersion(categories: Categories) = lazy { createFirstVersionOf(categories) }
        .takeIf { categories.isFirstVersion() }
        ?.value
        ?: updateVersionOf(categories)

    private fun createFirstVersionOf(categories: Categories) =
        "INSERT INTO CATEGORY_SETS (OWNER_ID, VERSION) VALUES (?, ?)"
            .let { jdbc.update(it, categories.id.value, 1) }
            .let { categories }

    private fun updateVersionOf(categories: Categories) = "UPDATE CATEGORY_SETS SET VERSION = ? WHERE OWNER_ID = ?"
        .let { jdbc.update(it, categories.version.value, categories.id.value) }
        .let { categories }

    private fun insert(categories: Categories) = categories
        .list
        .filter { it.isNew() }
        .forEach { insert(it, categories.id) }

    private fun insert(category: Category, ownerId: OwnerId) =
        "INSERT INTO CATEGORIES (NAME, ICON, OWNER_ID) VALUES (?, ?, ?)"
            .let { jdbc.update(it, category.name, category.icon.name, ownerId.value) }
}

private data class CategorySetEntryEntity(
    val id: Long,
    val name: String,
    val icon: Icon,
    val ownerId: OwnerId,
    val setVersion: Version
)

private class CategorySetEntryEntityMapper : RowMapper<CategorySetEntryEntity> {

    override fun mapRow(rs: ResultSet, rowNum: Int): CategorySetEntryEntity {
        return CategorySetEntryEntity(
            id = rs.getLong("ID"),
            name = rs.getString("NAME"),
            icon = rs.getString("ICON").toIcon(),
            ownerId = rs.getLong("OWNER_ID").toOwnerId(),
            setVersion = rs.getInt("VERSION").toVersion()
        )
    }
}

private fun String.toIcon() = Icon.valueOfOrNull(this) ?: Icon.FOLDER

private fun Long.toOwnerId() = ownerIdOf(this)

private fun Int.toVersion() = versionOf(this)