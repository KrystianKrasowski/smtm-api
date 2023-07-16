package com.smtm.application.spring.infrastructure.persistence

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.smtm.application.domain.OwnerId
import com.smtm.application.domain.Version
import com.smtm.application.domain.ownerIdOf
import com.smtm.application.domain.plans.PlanSummaries
import com.smtm.application.domain.plans.PlanSummariesProblem
import com.smtm.application.domain.plans.existingPlanSummaryOf
import com.smtm.application.domain.plans.fetchedPlanSummariesOf
import com.smtm.application.domain.versionOf
import com.smtm.application.spi.PlansRepository
import org.slf4j.LoggerFactory
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet
import java.time.Clock
import java.time.LocalDateTime

private val logger = LoggerFactory.getLogger(PlansRepositoryJdbcAdapter::class.java)

class PlansRepositoryJdbcAdapter(
    private val clock: Clock,
    private val jdbc: JdbcOperations
) : PlansRepository {

    private val selectAllPlanSummaries = """
        SELECT
            ps.owner_id,
            ps.version,
            p.id,
            p.name,
            p.start,
            p.end
        FROM plan_sets ps
        JOIN plans p ON p.owner_id = ps.owner_id
        WHERE ps.owner_id = ?
    """.trimIndent()

    override fun getAllPlanSummaries(ownerId: OwnerId): Either<PlanSummariesProblem, PlanSummaries> {
        return selectAllPlanSummaries
            .runCatching { jdbc.query(this, PlanEntityMapper(), ownerId.value) }
            .map { it.toPlanSummaries(clock, ownerId) }
            .map { it.right() }
            .onFailure { logger.error("Cannot fetch plan summaries", it) }
            .getOrElse { PlanSummariesProblem.RepositoryFailure.left() }
    }
}

private data class PlanEntity(
    val ownerId: OwnerId,
    val version: Version,
    val id: Long?,
    val name: String?,
    val start: LocalDateTime?,
    val end: LocalDateTime?
) {

    fun toPlanSummary() = existingPlanSummaryOf(
        id = id!!,
        name = name!!,
        start = start!!,
        end = end!!
    )
}

private class PlanEntityMapper : RowMapper<PlanEntity> {

    override fun mapRow(rs: ResultSet, rowNum: Int): PlanEntity {
        return PlanEntity(
            ownerId = ownerIdOf(rs.getLong("owner_id")),
            version = versionOf(rs.getInt("version")),
            id = rs.getLong("id"),
            name = rs.getString("name"),
            start = rs.getTimestamp("start").toLocalDateTime(),
            end = rs.getTimestamp("end").toLocalDateTime()
        )
    }
}

private fun List<PlanEntity>.toPlanSummaries(clock: Clock, ownerId: OwnerId) = fetchedPlanSummariesOf(
    clock = clock,
    id = firstOrNull()?.ownerId ?: ownerId,
    version = firstOrNull()?.version ?: versionOf(0),
    plans = map { it.toPlanSummary() }
)