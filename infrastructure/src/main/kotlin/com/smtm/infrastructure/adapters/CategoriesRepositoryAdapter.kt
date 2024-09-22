package com.smtm.infrastructure.adapters

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.smtm.core.domain.EntityId
import com.smtm.core.domain.Icon
import com.smtm.core.domain.OwnerId
import com.smtm.core.domain.Version
import com.smtm.core.domain.categories.Categories
import com.smtm.core.domain.categories.CategoriesProblem
import com.smtm.core.domain.categories.Category
import com.smtm.core.spi.CategoriesRepository
import com.smtm.infrastructure.persistence.categories.CategorySetViewRecord
import javax.sql.DataSource
import org.springframework.jdbc.core.JdbcTemplate

class CategoriesRepositoryAdapter(
    dataSource: DataSource,
    private val ownerIdProvider: () -> OwnerId,
) : CategoriesRepository {

    private val jdbc = JdbcTemplate(dataSource)

    override fun getCategories(): Either<CategoriesProblem, Categories> {
        return CategorySetViewRecord
            .runCatching { selectByOwnerId(ownerIdProvider().value, jdbc) }
            .map { it.toCategories(ownerIdProvider()) }
            .map { it.right() }
            .getOrElse { CategoriesProblem.failure(it).left() }
    }

    override fun save(categories: Categories): Either<CategoriesProblem, Categories> {
        TODO("Not yet implemented")
    }
}

private fun List<CategorySetViewRecord>.toCategories(ownerId: OwnerId): Categories =
    firstOrNull()
        ?.let { first ->
            Categories(
                id = OwnerId.of(first.ownerId),
                version = Version.of(first.version),
                actual = map { it.toCategory() }
            )
        }
        ?: Categories.empty(ownerId)

private fun CategorySetViewRecord.toCategory() =
    Category.of(
        id = EntityId.of(id!!),
        name = name!!,
        icon = Icon.valueOfOrDefault(icon!!)
    )
