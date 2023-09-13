package com.smtm.infrastructure.persistence.plans

import com.smtm.application.domain.Icon
import com.smtm.application.domain.NumericId
import com.smtm.application.domain.categories.Category
import com.smtm.application.domain.ownerIdOf
import com.smtm.application.domain.plans.Plan
import com.smtm.application.domain.plans.PlanDefinition
import com.smtm.application.domain.plans.PlannedCategory
import com.smtm.application.domain.versionOf
import com.smtm.infrastructure.persistence.categories.CategoryRecord
import com.smtm.infrastructure.persistence.categories.Conversions.toCategory
import com.smtm.infrastructure.persistence.toMonetaryAmount
import javax.money.MonetaryAmount
import org.springframework.jdbc.core.JdbcOperations
import java.sql.ResultSet
import java.time.LocalDateTime

internal data class PlannedCategoriesView(private val entries: Collection<Row>) {

    val empty: Boolean = entries.isEmpty()
    val ownerId: Long get() = first.ownerId
    private val first: Row get() = entries.first()

    fun toPlan(categories: List<CategoryRecord>): Plan =
        Plan(
            version = versionOf(first.version),
            ownerId = ownerIdOf(first.ownerId),
            definition = PlanDefinition(
                id = NumericId.of(first.id),
                name = first.name,
                period = first.start..first.end
            ),
            entries = entries.map {
                PlannedCategory(
                    category = Category.of(
                        id = it.categoryId,
                        name = it.categoryName,
                        icon = Icon.valueOfOrNull(it.categoryIcon) ?: Icon.FOLDER
                    ),
                    value = it.value
                )
            },
            newEntries = emptyList(),
            availableCategories = categories.map { it.toCategory() }
        )

    companion object {

        private val selectByPlanId = """
            SELECT
                p.id,
                p.owner_id,
                p.version,
                p.name as plan_name,
                p.start,
                p.end,
                c.id as category_id,
                c.name as category_name,
                c.icon as category_icon,
                pe.amount,
                pe.currency
            FROM plans p
            JOIN plan_entries pe ON pe.plan_id = p.id
            JOIN categories c ON c.id = pe.category_id
            WHERE p.id = ?
        """.trimIndent()

        private val MAPPER = { rs: ResultSet, _: Int ->
            Row(
                id = rs.getLong("id"),
                ownerId = rs.getLong("owner_id"),
                version = rs.getInt("version"),
                name = rs.getString("plan_name"),
                start = rs.getTimestamp("start").toLocalDateTime(),
                end = rs.getTimestamp("end").toLocalDateTime(),
                categoryId = rs.getLong("category_id"),
                categoryName = rs.getString("category_name"),
                categoryIcon = rs.getString("category_icon"),
                amount = rs.getInt("amount"),
                currency = rs.getString("currency")
            )
        }

        fun selectByPlanId(id: Long, jdbc: JdbcOperations): PlannedCategoriesView =
            jdbc.query(selectByPlanId, MAPPER, id)
                .let { PlannedCategoriesView(it) }
    }

    data class Row(
        val id: Long,
        val ownerId: Long,
        val version: Int,
        val name: String,
        val start: LocalDateTime,
        val end: LocalDateTime,
        val categoryId: Long,
        val categoryName: String,
        val categoryIcon: String,
        val amount: Int,
        val currency: String
    ) {

        val value: MonetaryAmount =
            amount.toMonetaryAmount(currency)
    }
}

