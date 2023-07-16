package com.smtm.application.spring.infrastructure.persistence

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.smtm.application.api.PlansQueries
import com.smtm.application.domain.OwnerId
import com.smtm.application.domain.Version
import com.smtm.application.domain.ownerIdOf
import com.smtm.application.domain.plans.PlanDefinition
import com.smtm.application.domain.plans.existingPlanDefinitionOf
import com.smtm.application.domain.versionOf
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
) : PlansQueries {

    private val selectCurrentPlans = """
        SELECT
            *
        FROM plans
        WHERE owner_id = ?
        AND start <= ?
        AND "end" >= ?
    """.trimIndent()

    private val selectUpcomingPlans = """
        SELECT
            *
        FROM plans
        WHERE owner_id = ?
        AND start > ?
    """.trimIndent()

    private val selectArchivedPlans = """
        SELECT
            *
        FROM plans
        WHERE owner_id = ?
        AND "end" < ?
    """.trimIndent()

    override fun getCurrentPlans(ownerId: OwnerId): Either<Throwable, List<PlanDefinition>> {
        return LocalDateTime.now(clock)
            .runCatching { jdbc.query(selectCurrentPlans, PlanEntityMapper(), ownerId.value, this, this) }
            .toQueryResult()
    }

    override fun getUpcomingPlans(ownerId: OwnerId): Either<Throwable, List<PlanDefinition>> {
        return LocalDateTime.now(clock)
            .runCatching { jdbc.query(selectUpcomingPlans, PlanEntityMapper(), ownerId.value, this) }
            .toQueryResult()
    }

    override fun getArchivedPlans(ownerId: OwnerId): Either<Throwable, List<PlanDefinition>> {
        return LocalDateTime.now(clock)
            .runCatching { jdbc.query(selectArchivedPlans, PlanEntityMapper(), ownerId.value, this) }
            .toQueryResult()
    }

    private fun Result<List<PlanEntity>>.toQueryResult() = map { it.toPlanDefinitions() }
        .map { it.right() }
        .onFailure { logger.error("Cannot fetch plan summaries", it) }
        .getOrElse { it.left() }
}

private data class PlanEntity(
    val ownerId: OwnerId,
    val version: Version,
    val id: Long?,
    val name: String?,
    val start: LocalDateTime?,
    val end: LocalDateTime?
) {

    fun toPlanDefinition() = existingPlanDefinitionOf(
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

private fun List<PlanEntity>.toPlanDefinitions() = map { it.toPlanDefinition() }