package com.smtm.infrastructure.persistence.categories

import com.smtm.core.domain.Icon
import com.smtm.core.domain.NumericId
import com.smtm.core.domain.OwnerId
import com.smtm.core.domain.Version
import com.smtm.core.domain.categories.Categories
import com.smtm.core.domain.categories.Category
import com.smtm.core.domain.toOwnerId
import com.smtm.core.domain.toVersion
import org.springframework.jdbc.core.JdbcOperations

internal object Conversions {

    fun List<CategoryInSetRecord>.toDomain(ownerId: OwnerId): Categories =
        if (isEmpty()) Categories.empty(ownerId)
        else Categories.fetched(extractId(), extractVersion(), extractCategoryList())

    fun Categories.toCategorySetRecord(jdbc: JdbcOperations): CategorySetRecord =
        CategorySetRecord(
            ownerId = id.value,
            version = version.value,
            jdbc = jdbc
        )

    fun Category.toCategoryRecord(ownerId: OwnerId, jdbc: JdbcOperations): CategoryRecord =
        CategoryRecord(
            id = id.valueOrNull,
            ownerId = ownerId.value,
            name = name,
            icon = icon.name,
            jdbc = jdbc
        )

    fun List<CategoryRecord>.toCategoryList(): List<Category> =
        map { it.toCategory() }

    fun CategoryRecord.toCategory(): Category =
        Category(
            id = NumericId.of(id),
            name = name,
            icon = Icon.valueOfOrDefault(icon)
        )

    private fun List<CategoryInSetRecord>.extractId(): OwnerId =
        first()
            .ownerId
            .toOwnerId()

    private fun List<CategoryInSetRecord>.extractVersion(): Version =
        first()
            .setVersion
            .toVersion()

    private fun List<CategoryInSetRecord>.extractCategoryList(): List<Category> =
        this.filter { !it.empty }
            .map {
                Category.of(
                    id = it.id!!,
                    name = it.name!!,
                    icon = Icon.valueOfOrDefault(it.icon!!)
                )
            }
}
