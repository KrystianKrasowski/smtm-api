package com.smtm.infrastructure.persistence.categories

import com.smtm.application.domain.Icon
import com.smtm.application.domain.NumericId
import com.smtm.application.domain.OwnerId
import com.smtm.application.domain.Version
import com.smtm.application.domain.categories.Categories
import com.smtm.application.domain.categories.Category
import com.smtm.application.domain.toOwnerId
import com.smtm.application.domain.toVersion
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
