package com.smtm.infrastructure.persistence.plans

import com.smtm.core.domain.EntityId
import com.smtm.core.domain.plans.PlanHeader
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet
import java.time.LocalDate

internal data class PlanRecord(
    val id: String,
    val ownerId: String,
    val version: Int,
    val name: String,
    val start: LocalDate,
    val end: LocalDate,
    private val jdbc: JdbcOperations
) {

    fun toPlanDefinition(): PlanHeader =
        PlanHeader(
            id = EntityId.of(id),
            name = name,
            period = start..end
        )

    companion object {

        fun selectByOwnerAndMatchingDate(
            ownerId: String,
            matchingDate: LocalDate,
            jdbc: JdbcOperations
        ): List<PlanRecord> =
            jdbc.query(
                """SELECT * FROM plans WHERE owner_id = ? AND ? BETWEEN start and "end" ORDER BY "end" DESC""",
                PlanRecordMapper(jdbc),
                ownerId, matchingDate
            )

        fun selectByOwnerId(ownerId: String, jdbc: JdbcOperations): List<PlanRecord> =
            jdbc.query(
                """SELECT * FROM plans WHERE owner_id = ? ORDER BY "end" DESC""",
                PlanRecordMapper(jdbc),
                ownerId
            )
    }
}

private class PlanRecordMapper(private val jdbc: JdbcOperations) : RowMapper<PlanRecord> {

    override fun mapRow(rs: ResultSet, rowNum: Int): PlanRecord =
        PlanRecord(
            id = rs.getString("id"),
            ownerId = rs.getString("owner_id"),
            version = rs.getInt("version"),
            name = rs.getString("name"),
            start = rs.getTimestamp("start").toLocalDateTime().toLocalDate(),
            end = rs.getTimestamp("end").toLocalDateTime().toLocalDate(),
            jdbc = jdbc
        )
}
