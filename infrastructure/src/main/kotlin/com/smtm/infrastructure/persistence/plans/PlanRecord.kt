package com.smtm.infrastructure.persistence.plans

import com.smtm.application.domain.NumericId
import com.smtm.application.domain.plans.Plan
import com.smtm.application.domain.plans.PlanDefinition
import com.smtm.infrastructure.persistence.insertAndGetId
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.jdbc.core.RowMapper
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement
import java.sql.Timestamp
import java.time.LocalDateTime

internal data class PlanRecord(
    val id: Long?,
    val ownerId: Long,
    val version: Int,
    val name: String,
    val start: LocalDateTime,
    val end: LocalDateTime,
    private val jdbc: JdbcOperations
) {

    fun upsert(): PlanRecord =
        if (id == null) insert()
        else update()

    fun toPlanDefinition(): PlanDefinition =
        PlanDefinition(
            id = NumericId.of(id),
            name = name,
            period = start..end
        )

    private fun insert(): PlanRecord =
        jdbc
            .insertAndGetId(prepareInsert())
            .let { copy(id = it) }

    private fun update(): PlanRecord =
        "UPDATE plans SET name = ?, start = ?, \"end\" = ? WHERE id = ?"
            .let { jdbc.update(it, name, start, end, id!!) }
            .let { this }

    private fun prepareInsert(): (Connection) -> PreparedStatement = { connection ->
        "INSERT INTO plans (owner_id, version, name, start, \"end\") VALUES (?, ?, ?, ?, ?)"
            .let { connection.prepareStatement(it, Statement.RETURN_GENERATED_KEYS) }
            .apply { setLong(1, ownerId) }
            .apply { setInt(2, version) }
            .apply { setString(3, name) }
            .apply { setTimestamp(4, Timestamp.valueOf(start)) }
            .apply { setTimestamp(5, Timestamp.valueOf(end)) }
    }

    companion object {

        fun getByOwnerIdAndPeriodAround(
            ownerId: Long,
            timestamp: LocalDateTime,
            jdbc: JdbcOperations
        ): List<PlanRecord> =
            "SELECT * FROM plans WHERE owner_id = ? AND start <= ? AND \"end\" >= ?"
                .let { jdbc.query(it, ResultSetMapper(jdbc), ownerId, timestamp, timestamp) }

        fun getByOwnerIdAndPeriodAfter(
            ownerId: Long,
            timestamp: LocalDateTime,
            jdbc: JdbcOperations
        ): List<PlanRecord> =
            "SELECT * FROM plans WHERE owner_id = ? AND start > ?"
                .let { jdbc.query(it, ResultSetMapper(jdbc), ownerId, timestamp) }

        fun getByOwnerIdAndPeriodBefore(
            ownerId: Long,
            timestamp: LocalDateTime,
            jdbc: JdbcOperations
        ): List<PlanRecord> =
            "SELECT * FROM plans WHERE owner_id = ? AND \"end\" < ?"
                .let { jdbc.query(it, ResultSetMapper(jdbc), ownerId, timestamp) }

        fun from(plan: Plan, jdbc: JdbcOperations): PlanRecord =
            PlanRecord(
                id = plan.id.valueOrNull,
                ownerId = plan.ownerId.value,
                version = plan.version.value,
                name = plan.name,
                start = plan.start,
                end = plan.end,
                jdbc = jdbc
            )
    }
}

private class ResultSetMapper(private val jdbc: JdbcOperations) : RowMapper<PlanRecord> {

    override fun mapRow(rs: ResultSet, rowNum: Int): PlanRecord =
        PlanRecord(
            id = rs.getLong("id"),
            ownerId = rs.getLong("owner_id"),
            version = rs.getInt("version"),
            name = rs.getString("name"),
            start = rs.getTimestamp("start").toLocalDateTime(),
            end = rs.getTimestamp("end").toLocalDateTime(),
            jdbc = jdbc
        )
}
