package com.smtm.infrastructure.persistence.categories

import org.springframework.jdbc.core.JdbcOperations

internal data class CategorySetRecord(
    val ownerId: Long,
    val version: Int,
    private val jdbc: JdbcOperations
) {

    fun upsert(): CategorySetRecord =
        if (version == 1) insert()
        else update()

    private fun insert(): CategorySetRecord =
        "INSERT INTO category_sets (owner_id, version) VALUES (?, ?)"
            .let { jdbc.update(it, ownerId, version) }
            .let { this }

    private fun update(): CategorySetRecord =
        "UPDATE category_sets SET version = ? WHERE owner_id = ?"
            .let { jdbc.update(it, version, ownerId) }
            .let { this }
}
