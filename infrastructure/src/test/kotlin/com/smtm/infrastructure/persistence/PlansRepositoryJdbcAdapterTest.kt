package com.smtm.infrastructure.persistence

import com.smtm.core.assert.SmtmAssertions
import com.smtm.core.domain.Icon
import com.smtm.core.domain.NumericId
import com.smtm.core.domain.Version
import com.smtm.core.domain.categories.Category
import com.smtm.core.domain.ownerIdOf
import com.smtm.core.domain.plans.Plan
import com.smtm.core.domain.plans.PlanDefinition
import com.smtm.core.domain.plans.PlannedCategory
import com.smtm.core.domain.plans.PlansProblem
import com.smtm.core.domain.toOwnerId
import com.smtm.core.domain.toVersion
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
        val plan = adapter.find(NumericId.of(1L)).getOrNull()

        // then
        SmtmAssertions.assertThat(plan)
            .hasVersion(1)
            .hasOwner(1)
            .hasName("June 2023")
            .hasStart("2023-06-01T00:00:00")
            .hasEnd("2023-06-30T23:59:59")
            .hasPlannedCategory("Rent", "PLN 315.99")
            .hasAvailableCategory(Category.of(1, "Rent", Icon.HOUSE))
    }

    @Test
    fun `should fetch existing plan on known id`() {
        // when
        val definition = PlanDefinition.existing(
            id = NumericId.of(1L),
            name = "June 2023",
            start = LocalDateTime.parse("2023-06-01T00:00:00"),
            end = LocalDateTime.parse("2023-06-30T23:59:59")
        )
        val plan = adapter.findOrPrepare(definition, 1L.toOwnerId()).getOrNull()

        // then
        SmtmAssertions.assertThat(plan)
            .hasVersion(1)
            .hasOwner(1)
            .hasName("June 2023")
            .hasStart("2023-06-01T00:00:00")
            .hasEnd("2023-06-30T23:59:59")
            .hasPlannedCategory("Rent", "PLN 315.99")
            .hasAvailableCategory(Category.of(1, "Rent", Icon.HOUSE))
    }

    @Test
    fun `should define the new plan on unsettled id`() {
        // when
        val definition = PlanDefinition.unsettled(
            name = "September 2023",
            start = LocalDateTime.parse("2023-09-01T00:00:00"),
            end = LocalDateTime.parse("2023-09-30T23:59:59")
        )
        val plan = adapter.findOrPrepare(definition, 1L.toOwnerId()).getOrNull()

        // then
        SmtmAssertions.assertThat(plan)
            .hasVersion(Version.ZERO)
            .hasOwner(1)
            .hasName("September 2023")
            .hasStart("2023-09-01T00:00:00")
            .hasEnd("2023-09-30T23:59:59")
            .hasNoPlannedCategories()
            .hasAvailableCategory(Category.of(1, "Rent", Icon.HOUSE))
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
            version = Version.ZERO,
            ownerId = ownerIdOf(1),
            definition = PlanDefinition.unsettled(
                name = "September 2023",
                start = LocalDateTime.parse("2023-09-01T00:00:00"),
                end = LocalDateTime.parse("2023-09-30T23:59:59")
            ),
            entries = listOf(
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
        SmtmAssertions.assertThat(result)
            .hasVersion(1)
            .hasOwner(1)
            .hasName("September 2023")
            .hasStart("2023-09-01T00:00:00")
            .hasEnd("2023-09-30T23:59:59")
            .hasPlannedCategory("Rent", "PLN 317.98")
            .hasAvailableCategory(Category.of(1, "Rent", Icon.HOUSE))
    }

    @Test
    fun `should update existing plan`() {
        // given
        val plan = Plan(
            version = 1.toVersion(),
            ownerId = 1.toOwnerId(),
            definition = PlanDefinition.existing(
                id = NumericId.of(3L),
                name = "Supreme August 2023",
                start = LocalDateTime.parse("2023-08-10T00:00:00"),
                end = LocalDateTime.parse("2023-08-30T23:59:59")
            ),
            entries = listOf(
                PlannedCategory(
                    category = Category.of(1, "Rent", Icon.HOUSE),
                    value = Money.of(517.98, "PLN")
                )
            ),
            availableCategories = emptyList()
        )

        // when
        val result = adapter.save(plan).getOrNull()

        // then
        SmtmAssertions.assertThat(result)
            .hasVersion(1)
            .hasOwner(1)
            .hasName("Supreme August 2023")
            .hasStart("2023-08-10T00:00:00")
            .hasEnd("2023-08-30T23:59:59")
            .hasOnlyPlannedCategory("Rent", "PLN 517.98")
            .hasAvailableCategory(Category.of(1, "Rent", Icon.HOUSE))
    }
}
