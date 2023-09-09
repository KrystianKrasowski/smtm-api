package com.smtm.infrastructure.persistence.plans

import com.smtm.application.domain.NumericId
import com.smtm.application.domain.plans.PlanDefinition
import org.springframework.jdbc.core.JdbcOperations
import java.sql.ResultSet
import java.time.LocalDateTime

internal data class PlansResultSet(private val rows: List<Row>) {

    fun toPlanDefinitionList(): List<PlanDefinition> =
        rows.map { it.toPlanDefinition() }

    data class Row(
        val id: Long,
        val ownerId: Long,
        val version: Int,
        val name: String,
        val start: LocalDateTime,
        val end: LocalDateTime
    ) {

        fun toPlanDefinition(): PlanDefinition =
            PlanDefinition(
                id = NumericId.of(id),
                name = name,
                period = start..end
            )
    }

    companion object {

        private val selectPlansByOwnerIdAndPeriodAround = """
            SELECT
                *
            FROM plans
            WHERE owner_id = ?
            AND start <= ?
            AND "end" >= ?
        """.trimIndent()

        private val selectPlansByOwnerIdAndPeriodAfter = """
            SELECT
                *
            FROM plans
            WHERE owner_id = ?
            AND start > ?
        """.trimIndent()

        private val selectPlansByOwnerIdAndPeriodBefore = """
            SELECT
                *
            FROM plans
            WHERE owner_id = ?
            AND "end" < ?
        """.trimIndent()

        private val MAPPER = { rs: ResultSet, _: Int ->
            Row(
                id = rs.getLong("id"),
                ownerId = rs.getLong("owner_id"),
                version = rs.getInt("version"),
                name = rs.getString("name"),
                start = rs.getTimestamp("start").toLocalDateTime(),
                end = rs.getTimestamp("end").toLocalDateTime()
            )
        }

        fun getByOwnerIdAndPeriodAround(ownerId: Long, timestamp: LocalDateTime, jdbc: JdbcOperations): PlansResultSet =
            jdbc.query(selectPlansByOwnerIdAndPeriodAround, MAPPER, ownerId, timestamp, timestamp)
                .let { PlansResultSet(it) }

        fun getByOwnerIdAndPeriodAfter(ownerId: Long, timestamp: LocalDateTime, jdbc: JdbcOperations): PlansResultSet =
            jdbc.query(selectPlansByOwnerIdAndPeriodAfter, MAPPER, ownerId, timestamp)
                .let { PlansResultSet(it) }

        fun getByOwnerIdAndPeriodBefore(ownerId: Long, timestamp: LocalDateTime, jdbc: JdbcOperations): PlansResultSet =
            jdbc.query(selectPlansByOwnerIdAndPeriodBefore, MAPPER, ownerId, timestamp)
                .let { PlansResultSet(it) }
    }
}
