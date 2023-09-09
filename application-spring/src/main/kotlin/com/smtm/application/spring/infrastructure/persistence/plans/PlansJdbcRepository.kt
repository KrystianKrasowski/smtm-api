package com.smtm.application.spring.infrastructure.persistence.plans

import com.smtm.application.domain.OwnerId
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.jdbc.support.GeneratedKeyHolder
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.Statement
import java.sql.Timestamp
import java.time.Clock
import java.time.LocalDateTime

internal class PlansJdbcRepository(
    private val clock: Clock,
    private val jdbc: JdbcOperations
) {

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

    private val selectPlansById = """
        SELECT 
            *
        FROM plans
        WHERE id = ?
    """.trimIndent()

    private val selectPlanEntriesByPlanId = """
        SELECT
            *
        FROM plan_entries
        WHERE plan_id = ?
    """.trimIndent()

    fun selectCurrentPlansByOwnerId(ownerId: OwnerId): List<PlanEntity> {
        return LocalDateTime.now(clock)
            .run { jdbc.query(selectCurrentPlans, PlanEntity.MAPPER, ownerId.value, this, this) }
    }

    fun selectUpcomingPlansByOwnerId(ownerId: OwnerId): List<PlanEntity> {
        return LocalDateTime.now(clock)
            .run { jdbc.query(selectUpcomingPlans, PlanEntity.MAPPER, ownerId.value, this) }
    }

    fun selectArchivedPlansByOwnerId(ownerId: OwnerId): List<PlanEntity> {
        return LocalDateTime.now(clock)
            .run { jdbc.query(selectArchivedPlans, PlanEntity.MAPPER, ownerId.value, this) }
    }

    fun upsert(planEntity: PlanEntity): PlanEntity =
        if (planEntity.id == null) insert(planEntity)
        else update(planEntity)

    fun insert(planEntry: PlanEntryEntity, holder: GeneratedKeyHolder = GeneratedKeyHolder()): PlanEntryEntity =
        jdbc.update(prepareInsert(planEntry), holder)
            .let { holder.keys?.get("id")?.toString()?.toLong() }
            ?.let { planEntry.copy(id = it) }
            ?: error("Something went wrong with plan entry insert")


    private fun insert(planEntity: PlanEntity, holder: GeneratedKeyHolder = GeneratedKeyHolder()): PlanEntity =
        jdbc.update(prepareInsertDefinition(planEntity), holder)
            .let { holder.keys?.get("id")?.toString()?.toLong() }
            ?.let { planEntity.copy(id = it) }
            ?: error("Something went wrong with plan definition insert")

    private fun update(plan: PlanEntity): PlanEntity =
        """UPDATE plans SET version = ?, name = ?, start = ?, "end" = ? WHERE id = ?"""
            .let { jdbc.update(it, plan.version.value, plan.name, plan.start, plan.end, plan.id) }
            .let { plan }

    private fun prepareInsertDefinition(plan: PlanEntity): (Connection) -> PreparedStatement = { connection ->
        connection
            .prepareStatement("""INSERT INTO plans (owner_id, version, name, start, "end") VALUES (?, ?, ?, ?, ?)""", Statement.RETURN_GENERATED_KEYS)
            .apply { setLong(1, plan.ownerId.value) }
            .apply { setInt(2, plan.version.value) }
            .apply { setString(3, plan.name) }
            .apply { setTimestamp(4, Timestamp.valueOf(plan.start)) }
            .apply { setTimestamp(5, Timestamp.valueOf(plan.end)) }
    }

    private fun prepareInsert(planEntry: PlanEntryEntity): (Connection) -> PreparedStatement = { connection ->
        connection
            .prepareStatement("""INSERT INTO plan_entries (plan_id, category_id, amount, currency) values (?, ?, ?, ?)""", Statement.RETURN_GENERATED_KEYS)
            .apply { setLong(1, planEntry.planId) }
            .apply { setLong(2, planEntry.categoryId) }
            .apply { setInt(3, planEntry.amount) }
            .apply { setString(4, planEntry.currency) }
    }
}
