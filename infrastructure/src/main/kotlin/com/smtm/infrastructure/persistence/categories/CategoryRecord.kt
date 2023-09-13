package com.smtm.infrastructure.persistence.categories

import org.springframework.jdbc.core.JdbcOperations
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

internal data class CategoryRecord(
    val id: Long?,
    val ownerId: Long,
    val name: String,
    val icon: String,
    private val jdbc: JdbcOperations
) {

    fun upsert(): CategoryRecord =
        if (id == null) insert()
        else update()

    fun delete() {
        "DELETE FROM categories WHERE id = ?"
            .let { jdbc.update(it, id) }
    }

    private fun insert(): CategoryRecord =
        "INSERT INTO categories (name, icon, owner_id) VALUES (?, ?, ?)"
            .let { jdbc.update(it, name, icon, ownerId) }
            .let { this }

    private fun update(): CategoryRecord =
        "UPDATE categories SET name = ?, icon = ? WHERE id = ?"
            .let { jdbc.update(it, name, icon, id) }
            .let { this }

    companion object {

        fun selectByOwnerId(ownerId: Long, jdbc: JdbcOperations): List<CategoryRecord> =
            "SELECT * FROM categories WHERE owner_id = ?"
                .let { jdbc.query(it, CategoryRecordMapper(jdbc), ownerId) }
    }
}

private class CategoryRecordMapper(private val jdbc: JdbcOperations) : RowMapper<CategoryRecord> {

    override fun mapRow(rs: ResultSet, rowNum: Int): CategoryRecord {
        return CategoryRecord(
            id = rs.getLong("id"),
            ownerId = rs.getLong("owner_id"),
            name = rs.getString("name"),
            icon = rs.getString("icon"),
            jdbc = jdbc
        )
    }
}
