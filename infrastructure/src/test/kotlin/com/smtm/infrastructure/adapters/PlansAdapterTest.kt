package com.smtm.infrastructure.adapters

import arrow.core.getOrElse
import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import com.smtm.core.api.PlansQueries
import com.smtm.core.domain.EntityId
import com.smtm.core.domain.Icon
import com.smtm.core.domain.NumericId
import com.smtm.core.domain.OwnerId
import com.smtm.core.domain.categories.Category
import com.smtm.core.domain.plans.Plan
import com.smtm.core.domain.plans.PlanHeader
import com.smtm.infrastructure.persistence.TestDatabase
import com.smtm.infrastructure.persistence.runSql
import javax.money.MonetaryAmount
import javax.sql.DataSource
import org.javamoney.moneta.Money
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.time.LocalDate

class PlansAdapterTest {

    private val dataSource: DataSource = TestDatabase.setup()
    private val plansAdapter get() = PlansAdapter(dataSource)

    @BeforeEach
    fun setUp() {
        dataSource.runSql("INSERT INTO category_sets (owner_id, version) VALUES (1, 1)")
        dataSource.runSql("""
            INSERT INTO categories (owner_id, name, icon)
            VALUES
            (1, 'Rent', 'HOUSE'),
            (1, 'Savings', 'PIGGY_BANK'),
            (1, 'Groceries', 'SHOPPING_CART')
        """.trimIndent())
        dataSource.runSql("""
            INSERT INTO plans (id, owner_id, version, name, start, "end") 
            VALUES 
            ('smtm-plan-1', 1, 1, 'August 2024', '2024-08-01', '2024-08-31'),
            ('smtm-plan-2', 1, 1, 'September 2024', '2024-09-01', '2024-09-30'),
            ('smtm-plan-3', 1, 1, 'October 2024', '2024-10-01', '2024-10-31')
        """.trimIndent())
        dataSource.runSql("""
            INSERT INTO plan_entries (plan_id, category_id, amount, currency)
            VALUES
            ('smtm-plan-2', 1, 37959, 'PLN'),
            ('smtm-plan-2', 2, 500000, 'PLN'),
            ('smtm-plan-2', 3, 100000, 'PLN')
        """.trimIndent())
    }

    @AfterEach
    fun tearDown() {
        dataSource.runSql("DELETE FROM plans CASCADE")
        dataSource.runSql("DELETE FROM category_sets CASCADE")
        dataSource.runSql("ALTER TABLE plan_entries ALTER COLUMN id RESTART WITH 1")
        dataSource.runSql("ALTER TABLE categories ALTER COLUMN id RESTART WITH 1")
    }

    @ParameterizedTest
    @ValueSource(strings = [
        "2024-09-01",
        "2024-09-06",
        "2024-09-11",
        "2024-09-17",
        "2024-09-22",
        "2024-09-27",
        "2024-09-30",
    ])
    fun `should return plans by matching date`(dateWithinPeriod: String) {
        // when
        val criteria = PlansQueries.Criteria.by(
            owner = OwnerId(1),
            dateWithinPeriod = LocalDate.parse(dateWithinPeriod)
        )
        val result = plansAdapter
            .getPlanHeadersBy(criteria)
            .getOrElse { throw IllegalStateException("Expected to return success here", it) }

        assertThat(result).contains(
            planDefinitionOf("smtm-plan-2", "September 2024", "2024-09-01", "2024-09-30")
        )
    }

    @Test
    fun `should return all owner's plans`() {
        // when
        val criteria = PlansQueries.Criteria.by(
            owner = OwnerId(1)
        )
        val result = plansAdapter
            .getPlanHeadersBy(criteria)
            .getOrElse { throw IllegalStateException("Expected to return success here", it) }

        assertThat(result).containsExactly(
            planDefinitionOf("smtm-plan-3", "October 2024", "2024-10-01", "2024-10-31"),
            planDefinitionOf("smtm-plan-2", "September 2024", "2024-09-01", "2024-09-30"),
            planDefinitionOf("smtm-plan-1", "August 2024", "2024-08-01", "2024-08-31")
        )
    }

    @Test
    fun `should return owner's plan by id`() {
        // when
        val result = plansAdapter
            .getPlan(EntityId.of("smtm-plan-2"), OwnerId.of(1L))
            .getOrElse { error("Expected to return success here: $it") }

        // then
        assertThat(result.id).isEqualTo(EntityId.of("smtm-plan-2"))
        assertThat(result.start).isEqualTo(LocalDate.parse("2024-09-01"))
        assertThat(result.end).isEqualTo(LocalDate.parse("2024-09-30"))
        assertThat(result.entries).containsExactly(
            planEntryOf(categoryOf(1, "Rent", Icon.HOUSE), Money.of(379.59, "PLN")),
            planEntryOf(categoryOf(2, "Savings", Icon.PIGGY_BANK), Money.of(5000, "PLN")),
            planEntryOf(categoryOf(3, "Groceries", Icon.SHOPPING_CART), Money.of(1000, "PLN"))
        )
    }
}

private fun planDefinitionOf(id: String, name: String, start: String, end: String): PlanHeader =
    PlanHeader(
        id = EntityId.of(id),
        name = name,
        period = LocalDate.parse(start)..LocalDate.parse(end),
    )

private fun planEntryOf(category: Category, value: MonetaryAmount): Plan.Entry =
    Plan.Entry(
        category = category,
        value = value
    )

private fun categoryOf(id: Long, name: String, icon: Icon): Category =
    Category(
        id = NumericId.of(id),
        name = name,
        icon = icon
    )
