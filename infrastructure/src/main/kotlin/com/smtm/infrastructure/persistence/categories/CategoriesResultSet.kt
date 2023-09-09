package com.smtm.infrastructure.persistence.categories

import com.smtm.application.domain.Icon
import com.smtm.application.domain.NumericId
import com.smtm.application.domain.categories.Category
import org.springframework.jdbc.core.JdbcOperations
import java.sql.ResultSet

internal data class CategoriesResultSet(private val rows: List<Row>) {

    fun toCategoryList(): List<Category> =
        rows.map { it.toCategory() }

    data class Row(
        val id: Long,
        val ownerId: Long,
        val name: String,
        val icon: String
    ) {

        fun toCategory() = Category(
            id = NumericId.of(id),
            name = name,
            icon = Icon.valueOfOrDefault(icon)
        )
    }

    companion object {

        private val selectByOwnerId = """
            SELECT *
            FROM categories
            WHERE owner_id = ?
        """.trimIndent()

        private val MAPPER = { rs: ResultSet, _: Int ->
            Row(
                id = rs.getLong("id"),
                ownerId = rs.getLong("owner_id"),
                name = rs.getString("name"),
                icon = rs.getString("icon")
            )
        }

        fun selectByOwnerId(id: Long, jdbc: JdbcOperations): CategoriesResultSet =
            jdbc.query(selectByOwnerId, MAPPER, id)
                .let { CategoriesResultSet(it) }
    }
}
