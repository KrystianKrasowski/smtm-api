package com.smtm.infrastructure.persistence.plans

import org.springframework.jdbc.core.JdbcOperations
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet
import java.time.LocalDate

internal data class FullPlanViewRecord(
    val planId: String,
    val ownerId: Long,
    val version: Int,
    val name: String,
    val start: LocalDate,
    val end: LocalDate,
    val categoryId: Long,
    val categoryName: String,
    val categoryIcon: String,
    val amount: Int,
    val currency: String,
    private val jdbc: JdbcOperations
) {

    companion object {

        fun selectByIdAndOwner(id: String, ownerId: Long, jdbc: JdbcOperations): List<FullPlanViewRecord> =
            jdbc.query(
                """
                    SELECT
                        p.id as plan_id,
                        p.owner_id as owner_id,
                        p.version as version,
                        p.name as name,
                        p.start as start,
                        p.end as "end",
                        c.id as category_id,
                        c.name as category_name,
                        c.icon as category_icon,
                        e.amount as amount,
                        e.currency as currency
                    FROM plans p
                    JOIN plan_entries e ON e.plan_id = p.id
                    JOIN categories c ON c.id = e.category_id
                    WHERE p.id = ?
                    AND p.owner_id = ?
                """.trimIndent(),
                RecordMapper(jdbc),
                id, ownerId
            )
    }
}

private class RecordMapper(private val jdbc: JdbcOperations) : RowMapper<FullPlanViewRecord> {

    override fun mapRow(rs: ResultSet, rowNum: Int): FullPlanViewRecord =
        FullPlanViewRecord(
            planId = rs.getString("plan_id"),
            ownerId = rs.getLong("owner_id"),
            version = rs.getInt("version"),
            name = rs.getString("name"),
            start = rs.getTimestamp("start").toLocalDateTime().toLocalDate(),
            end = rs.getTimestamp("end").toLocalDateTime().toLocalDate(),
            categoryId = rs.getLong("category_id"),
            categoryName = rs.getString("category_name"),
            categoryIcon = rs.getString("category_icon"),
            amount = rs.getInt("amount"),
            currency = rs.getString("currency"),
            jdbc = jdbc
        )
}
