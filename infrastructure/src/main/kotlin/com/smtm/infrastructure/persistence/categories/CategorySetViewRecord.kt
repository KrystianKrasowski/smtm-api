package com.smtm.infrastructure.persistence.categories

import org.springframework.jdbc.core.JdbcOperations
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

internal data class CategorySetViewRecord(
    val ownerId: String,
    val version: Int,
    val id: String?,
    val name: String?,
    val icon: String?,
    private val jdbc: JdbcOperations
) {

    companion object {

        fun selectByOwnerId(ownerId: String, jdbc: JdbcOperations): List<CategorySetViewRecord> =
            jdbc.query(
                """
                SELECT
                    cs.owner_id,
                    cs.version,
                    c.id,
                    c.name,
                    c.icon
                FROM category_sets cs
                LEFT JOIN categories c ON c.owner_id = cs.owner_id
                WHERE cs.owner_id = ?
            """.trimIndent(),
                CategorySetViewRecordMapper(jdbc),
                ownerId
            )
    }
}

private class CategorySetViewRecordMapper(private val jdbc: JdbcOperations) : RowMapper<CategorySetViewRecord> {

    override fun mapRow(rs: ResultSet, rowNum: Int): CategorySetViewRecord =
        CategorySetViewRecord(
            ownerId = rs.getString("owner_id"),
            version = rs.getInt("version"),
            id = rs.getString("id"),
            name = rs.getString("name"),
            icon = rs.getString("icon"),
            jdbc = jdbc
        )
}
