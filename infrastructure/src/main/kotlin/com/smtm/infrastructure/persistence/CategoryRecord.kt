package com.smtm.infrastructure.persistence

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

data class CategoryRecord(
    val id: Long?,
    val ownerId: Long,
    val name: String,
    val icon: String,
    private val connection: Connection
) {

    fun save(): CategoryRecord =
        if (id == null) insert()
        else update()

    private fun insert(): CategoryRecord =
        connection
            .prepareStatement(
                "INSERT INTO categories (owner_id, name, icon) VALUES (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            )
            .apply { setLong(1, ownerId) }
            .apply { setString(2, name) }
            .apply { setString(3, icon) }
            .executeAndGetIdentity()
            .let { copy(id = it) }

    private fun update(): CategoryRecord =
        connection
            .prepareStatement("UPDATE categories SET name = ?, icon = ? WHERE id = ?")
            .apply { setString(1, name) }
            .apply { setString(2, icon) }
            .apply { setLong(3, checkNotNull(id)) }
            .executeUpdate()
            .let { this }

    companion object {

        fun findAllByOwnerId(ownerId: Long, connection: Connection): List<CategoryRecord> =
            connection
                .prepareStatement("SELECT * FROM categories WHERE owner_id = ?")
                .apply { setLong(1, ownerId) }
                .executeQuery()
                .toCategoryRecordList(connection)
    }
}

private fun PreparedStatement.executeAndGetIdentity(): Long =
    generatedKeys
        .takeIf { it.next() }
        ?.getLong(1)
        ?: error("There is no generated identity")

private fun ResultSet.toCategoryRecordList(connection: Connection): List<CategoryRecord> {
    val categories = mutableListOf<CategoryRecord>()
    while (next()) {
        categories.add(CategoryRecord(
            id = getLong("id"),
            ownerId = getLong("owner_id"),
            name = getString("name"),
            icon = getString("icon"),
            connection = connection
        ))
    }
    return categories.toList()
}
