package com.smtm.infrastructure.persistence

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.smtm.application.domain.Icon
import com.smtm.application.domain.OwnerId
import com.smtm.application.domain.Version
import com.smtm.application.domain.categories.Categories
import com.smtm.application.domain.categories.CategoriesProblem
import com.smtm.application.domain.categories.Category
import com.smtm.application.domain.ownerIdOf
import com.smtm.application.domain.versionOf
import com.smtm.application.spi.CategoriesRepository
import javax.sql.DataSource
import org.slf4j.LoggerFactory
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.transaction.support.TransactionTemplate
import java.sql.ResultSet

class CategoriesRepositoryJdbcAdapter(
    dataSource: DataSource
) : CategoriesRepository {

    private val jdbc = JdbcTemplate(dataSource)
    private val transactions = TransactionTemplate(DataSourceTransactionManager(dataSource))

    override fun getCategories(ownerId: OwnerId): Either<CategoriesProblem, Categories> {
        return "SELECT CS.OWNER_ID, CS.VERSION, C.ID, C.NAME, C.ICON FROM CATEGORY_SETS CS LEFT JOIN CATEGORIES C ON C.OWNER_ID = CS.OWNER_ID WHERE CS.OWNER_ID = ?"
            .runCatching { jdbc.query(this, CategorySetEntryEntityMapper(), ownerId.value) }
            .mapCatching { it.toCategories(ownerId) }
            .map { it.right() }
            .onFailure { logger.error("Cannot fetch categories", it) }
            .getOrElse { otherCategoriesProblemOf("Cannot fetch categories") }
    }

    override fun save(categories: Categories): Either<CategoriesProblem, Categories> = categories
        .takeIf { it.isModificationNeeded() }
        ?.applyChanges()
        ?: categories.right()

    private fun Categories.isModificationNeeded() = toDelete.isNotEmpty() || toSave.isNotEmpty()

    private fun Categories.applyChanges() = transactions.execute { trn ->
        this
            .runCatching { incrementVersion(this) }
            .mapCatching { it.insertAllMarked() }
            .mapCatching { it.updateAllMarked() }
            .mapCatching { it.deleteAllMarked() }
            .onFailure { trn.setRollbackOnly() }
            .onFailure { logger.error("Cannot save categories", it) }
            .map { getCategories(id) }
            .getOrElse { otherCategoriesProblemOf("Cannot save categories") }
    }

    private fun incrementVersion(categories: Categories) = lazy { createFirstVersionOf(categories) }
        .takeIf { categories.isFirstVersion() }
        ?.value
        ?: updateVersionOf(categories)

    private fun Categories.insertAllMarked() = apply {
        toSave
            .filter { it.id.isUnsettled() }
            .forEach { insert(it, id) }
    }

    private fun Categories.updateAllMarked() = apply {
        toSave
            .filter { it.id.isSettled() }
            .forEach { update(it) }
    }

    private fun Categories.deleteAllMarked() = apply {
        toDelete
            .forEach { delete(it) }
    }

    private fun otherCategoriesProblemOf(message: String) = CategoriesProblem.Other(
        message
    ).left()

    private fun createFirstVersionOf(categories: Categories) =
        "INSERT INTO CATEGORY_SETS (OWNER_ID, VERSION) VALUES (?, ?)"
            .let { jdbc.update(it, categories.id.value, 1) }
            .let { categories }

    private fun updateVersionOf(categories: Categories) =
        "UPDATE CATEGORY_SETS SET VERSION = ? WHERE OWNER_ID = ?"
            .let { jdbc.update(it, categories.version.value, categories.id.value) }
            .let { categories }

    private fun insert(category: Category, ownerId: OwnerId) =
        "INSERT INTO CATEGORIES (NAME, ICON, OWNER_ID) VALUES (?, ?, ?)"
            .let { jdbc.update(it, category.name, category.icon.name, ownerId.value) }

    private fun update(category: Category) =
        "UPDATE CATEGORIES SET NAME = ?, ICON = ? WHERE ID = ?"
            .let { jdbc.update(it, category.name, category.icon.name, category.id.value) }

    private fun delete(category: Category) =
        "DELETE FROM CATEGORIES WHERE ID = ?"
            .let { jdbc.update(it, category.id.value) }
}

private val logger = LoggerFactory.getLogger(CategoriesRepositoryJdbcAdapter::class.java)

private data class CategorySetEntryEntity(
    val id: Long?,
    val name: String?,
    val icon: Icon?,
    val ownerId: OwnerId,
    val setVersion: Version
) {

    fun toDomain(): Category? = lazy {
        Category.of(
            id!!, name = name!!,
            icon = icon!!
        )
    }
        .takeIf { id != null && name != null && icon != null }
        ?.value
}

private class CategorySetEntryEntityMapper : RowMapper<CategorySetEntryEntity> {

    override fun mapRow(rs: ResultSet, rowNum: Int): CategorySetEntryEntity {
        return CategorySetEntryEntity(
            id = rs.getLong("ID"),
            name = rs.getString("NAME"),
            icon = rs.getString("ICON")?.toIcon(),
            ownerId = rs.getLong("OWNER_ID").toOwnerId(),
            setVersion = rs.getInt("VERSION").toVersion()
        )
    }
}

private fun List<CategorySetEntryEntity>.toCategories(ownerId: OwnerId) = Categories.fetched(
    id = firstOrNull()?.ownerId ?: ownerId,
    version = firstOrNull()?.setVersion ?: versionOf(0),
    list = mapNotNull { it.toDomain() }
)

private fun String.toIcon() = Icon.valueOfOrNull(this) ?: Icon.FOLDER

private fun Long.toOwnerId() = ownerIdOf(this)

private fun Int.toVersion() = versionOf(this)

private fun Categories.isFirstVersion() = version.value == 1
