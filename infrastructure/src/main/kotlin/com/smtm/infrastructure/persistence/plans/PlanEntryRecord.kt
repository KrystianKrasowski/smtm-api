package com.smtm.infrastructure.persistence.plans

import com.smtm.core.domain.plans.Plan
import com.smtm.core.domain.plans.PlannedCategory
import com.smtm.infrastructure.persistence.insertAndGetId
import com.smtm.infrastructure.persistence.toCents
import org.springframework.jdbc.core.JdbcOperations
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.Statement

internal data class PlanEntryRecord(
    val id: Long?,
    val planId: Long,
    val categoryId: Long,
    val amount: Int,
    val currency: String,
    private val jdbc: JdbcOperations
) {

    fun insert(): PlanEntryRecord =
        jdbc
            .insertAndGetId(prepareInsert())
            .let { this.copy(id = it) }

    private fun prepareInsert(): (Connection) -> PreparedStatement = { connection ->
        "INSERT INTO plan_entries (plan_id, category_id, amount, currency) values (?, ?, ?, ?)"
            .let { connection.prepareStatement(it, Statement.RETURN_GENERATED_KEYS) }
            .apply { setLong(1, planId) }
            .apply { setLong(2, categoryId) }
            .apply { setInt(3, amount) }
            .apply { setString(4, currency) }
    }

    companion object {

        fun from(plannedCategory: PlannedCategory, plan: Plan, jdbc: JdbcOperations): PlanEntryRecord =
            PlanEntryRecord(
                id = null,
                planId = plan.id.value,
                categoryId = plannedCategory.category.id.value,
                amount = plannedCategory.value.toCents(),
                currency = plannedCategory.value.currency.currencyCode,
                jdbc = jdbc
            )

        fun deleteByPlanId(id: Long, jdbc: JdbcOperations) {
            "DELETE FROM plan_entries WHERE plan_id = ?"
                .let { jdbc.update(it, id) }
        }
    }
}
