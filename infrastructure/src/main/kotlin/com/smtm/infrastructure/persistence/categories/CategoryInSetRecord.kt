package com.smtm.infrastructure.persistence.categories

import com.smtm.core.domain.OwnerId
import org.springframework.jdbc.core.JdbcOperations
import java.sql.ResultSet

internal data class CategoryInSetRecord(
    val ownerId: Long,
    val setVersion: Int,
    val id: Long?,
    val name: String?,
    val icon: String?
) {

    val empty: Boolean =
        id == null || name == null || icon == null

    companion object {

    private val selectByOwnerIdQuery = """
        SELECT
            cs.owner_id, 
            cs.version, 
            c.id, 
            c.name, 
            c.icon
        FROM category_sets cs
        LEFT JOIN categories c on c.owner_id = cs.owner_id
        WHERE cs.owner_id = ?
    """.trimIndent()

        private val mapper = { rs: ResultSet, _: Int ->
            CategoryInSetRecord(
                ownerId = rs.getLong("owner_id"),
                setVersion = rs.getInt("version"),
                id = rs.getLong("id"),
                name = rs.getString("name"),
                icon = rs.getString("icon")
            )
        }

        fun getByOwnerId(ownerId: OwnerId, jdbc: JdbcOperations): List<CategoryInSetRecord> =
            jdbc.query(selectByOwnerIdQuery, mapper, ownerId.value)
    }
}
