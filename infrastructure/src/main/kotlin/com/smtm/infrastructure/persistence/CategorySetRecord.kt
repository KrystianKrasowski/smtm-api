package com.smtm.infrastructure.persistence

import java.sql.Connection
import java.sql.ResultSet

data class CategorySetRecord(
    val ownerId: Long,
    val version: Int,
    private val connection: Connection
) {

    fun save(): CategorySetRecord =
        if (version == 1) insert()
        else update()

    private fun insert(): CategorySetRecord =
        connection
            .prepareStatement("INSERT INTO category_sets (owner_id, version) VALUES (?, ?)")
            .apply { setLong(1, ownerId) }
            .apply { setInt(2, version) }
            .executeUpdate()
            .let { this }

    private fun update(): CategorySetRecord =
        connection
            .prepareStatement("UPDATE category_sets SET version = ? WHERE owner_id = ?")
            .apply { setInt(1, version) }
            .apply { setLong(2, ownerId) }
            .executeUpdate()
            .let { this }

    companion object {

        fun getOneByOwnerId(ownerId: Long, connection: Connection): CategorySetRecord =
            connection
                .prepareStatement("SELECT * FROM category_sets WHERE owner_id = ?")
                .apply { setLong(1, ownerId) }
                .executeQuery()
                .toCategorySetRecord(connection)
                ?: error("There is no category set for owner of id $ownerId")
    }
}

private fun ResultSet.toCategorySetRecord(connection: Connection): CategorySetRecord? =
    this.takeIf { next() }
        ?.let { CategorySetRecord(it.getLong("owner_id"), it.getInt("version"), connection) }
