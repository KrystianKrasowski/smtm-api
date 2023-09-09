package com.smtm.infrastructure.persistence

import com.smtm.application.domain.Icon
import com.smtm.application.domain.NumericId
import com.smtm.application.domain.categories.Category
import com.smtm.application.domain.ownerIdOf
import com.smtm.application.domain.plans.Plan
import com.smtm.application.domain.plans.PlanDefinition
import com.smtm.application.domain.plans.PlannedCategory
import com.smtm.application.domain.plans.PlansProblem
import com.smtm.application.domain.versionOf
import javax.sql.DataSource
import org.assertj.core.api.Assertions.assertThat
import org.javamoney.moneta.Money
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class PlansRepositoryJdbcAdapterTest {

    private lateinit var dataSource: DataSource

    private val clock = Clock.fixed(Instant.parse("2023-07-15T12:30:00.00Z"), ZoneId.of("UTC"))

    private val adapter get() = PlansRepositoryJdbcAdapter(dataSource, clock)

    private val initialSqlQueries = listOf(
        "insert into category_sets (owner_id, version) values (1, 1)",
        "insert into categories (owner_id, name, icon) values (1, 'Rent', 'HOUSE')",
        "insert into plans (owner_id, version, name, start, \"end\") values (1, 1, 'June 2023', timestamp '2023-06-01 00:00:00', timestamp '2023-06-30 23:59:59')",
        "insert into plans (owner_id, version, name, start, \"end\") values (1, 1, 'July 2023', timestamp '2023-07-01 00:00:00', timestamp '2023-07-31 23:59:59')",
        "insert into plans (owner_id, version, name, start, \"end\") values (1, 1, 'August 2023', timestamp '2023-08-01 00:00:00', timestamp '2023-08-31 23:59:59')",
        "insert into plan_entries (plan_id, category_id, amount, currency) values (1, 1, 31599, 'PLN')",
        "insert into plan_entries (plan_id, category_id, amount, currency) values (2, 1, 31599, 'PLN')",
        "insert into plan_entries (plan_id, category_id, amount, currency) values (3, 1, 31599, 'PLN')",
    )

    @BeforeEach
    fun setUp() {
        dataSource = TestDatabase.setup()
        initialSqlQueries.forEach(dataSource::runSql)
    }

    @AfterEach
    fun tearDown() {
        dataSource.runSql("delete from plan_entries")
        dataSource.runSql("delete from plans")
        dataSource.runSql("alter table plans alter column id restart with 1")
        dataSource.runSql("delete from categories")
        dataSource.runSql("delete from category_sets")
        dataSource.runSql("alter table categories alter column id restart with 1")
    }

    @Test
    fun `should get current plans`() {
        // when
        val plans = adapter.getCurrentPlans(ownerIdOf(1)).getOrNull()

        // then
        assertThat(plans).containsExactly(
            PlanDefinition.existing(
                id = NumericId.of(2L),
                name = "July 2023",
                start = LocalDateTime.parse("2023-07-01T00:00:00"),
                end = LocalDateTime.parse("2023-07-31T23:59:59")
            )
        )
    }

    @Test
    fun `should get upcoming plans`() {
        // when
        val plans = adapter.getUpcomingPlans(ownerIdOf(1)).getOrNull()

        // then
        assertThat(plans).containsExactly(
            PlanDefinition.existing(
                id = NumericId.of(3L),
                name = "August 2023",
                start = LocalDateTime.parse("2023-08-01T00:00:00"),
                end = LocalDateTime.parse("2023-08-31T23:59:59")
            )
        )
    }

    @Test
    fun `should get archived plans`() {
        // when
        val plans = adapter.getArchivedPlans(ownerIdOf(1)).getOrNull()

        // then
        assertThat(plans).containsExactly(
            PlanDefinition.existing(
                id = NumericId.of(1L),
                name = "June 2023",
                start = LocalDateTime.parse("2023-06-01T00:00:00"),
                end = LocalDateTime.parse("2023-06-30T23:59:59")
            ),
        )
    }

    @Test
    fun `should find plan by id`() {
        // when
        val result = adapter.find(NumericId.of(1L)).getOrNull()

        // then
        assertThat(result?.name).isEqualTo("June 2023")
        assertThat(result?.start).isEqualTo(LocalDateTime.parse("2023-06-01T00:00:00"))
        assertThat(result?.end).isEqualTo(LocalDateTime.parse("2023-06-30T23:59:59"))
        assertThat(result?.entries).contains(
            PlannedCategory(
                category = Category.of(1, "Rent", Icon.HOUSE),
                value = Money.of(315.99, "PLN")
            )
        )
    }

    @Test
    fun `should not find unknown plan`() {
        // when
        var problem: PlansProblem? = null
        adapter.find(NumericId.of(4L))
            .onLeft { problem = it }

        // then
        assertThat(problem).isInstanceOf(PlansProblem.UnknownPlan::class.java)
    }

    @Test
    fun `should save the new plan`() {
        // given
        val plan = Plan(
            version = versionOf(1),
            ownerId = ownerIdOf(1),
            definition = PlanDefinition.existing(
                id = NumericId.UNSETTLED,
                name = "September 2023",
                start = LocalDateTime.parse("2023-09-01T00:00:00"),
                end = LocalDateTime.parse("2023-09-30T23:59:59")
            ),
            entries = emptyList(),
            newEntries = listOf(
                PlannedCategory(
                    category = Category.of(1, "Rent", Icon.HOUSE),
                    value = Money.of(317.98, "PLN")
                )
            ),
            availableCategories = emptyList()
        )

        // when
        val result = adapter.save(plan).getOrNull()

        // then
        assertThat(result?.id).isEqualTo(NumericId.of(4L))
        assertThat(result?.name).isEqualTo("September 2023")
        assertThat(result?.start).isEqualTo(LocalDateTime.parse("2023-09-01T00:00:00"))
        assertThat(result?.end).isEqualTo(LocalDateTime.parse("2023-09-30T23:59:59"))
        assertThat(result?.entries).contains(
            PlannedCategory(
                category = Category.of(1, "Rent", Icon.HOUSE),
                value = Money.of(317.98, "PLN")
            )
        )
    }
}