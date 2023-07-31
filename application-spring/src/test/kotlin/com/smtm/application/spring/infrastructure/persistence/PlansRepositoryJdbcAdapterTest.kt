package com.smtm.application.spring.infrastructure.persistence

import com.smtm.application.domain.Icon
import com.smtm.application.domain.categories.Category
import com.smtm.application.domain.ownerIdOf
import com.smtm.application.domain.plans.Plan
import com.smtm.application.domain.plans.PlanDefinition
import com.smtm.application.domain.plans.PlanId
import com.smtm.application.domain.plans.PlannedCategory
import com.smtm.application.domain.plans.toPlanId
import com.smtm.application.domain.versionOf
import org.assertj.core.api.Assertions.assertThat
import org.javamoney.moneta.Money
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.transaction.support.TransactionOperations
import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@SpringBootTest
class PlansRepositoryJdbcAdapterTest {

    @Autowired
    private lateinit var jdbc: JdbcOperations

    @Autowired
    private lateinit var transactions: TransactionOperations

    private val clock = Clock.fixed(Instant.parse("2023-07-15T12:30:00.00Z"), ZoneId.of("UTC"))

    private val adapter get() = PlansRepositoryJdbcAdapter(clock, jdbc, transactions)

    private val initialSqlQueries = listOf(
        "insert into plans (owner_id, version, name, start, \"end\") values (1, 1, 'June 2023', timestamp '2023-06-01 00:00:00', timestamp '2023-06-30 23:59:59')",
        "insert into plans (owner_id, version, name, start, \"end\") values (1, 1, 'July 2023', timestamp '2023-07-01 00:00:00', timestamp '2023-07-31 23:59:59')",
        "insert into plans (owner_id, version, name, start, \"end\") values (1, 1, 'August 2023', timestamp '2023-08-01 00:00:00', timestamp '2023-08-31 23:59:59')",
        "insert into category_sets (owner_id, version) values (1, 1)",
        "insert into categories (owner_id, name, icon) values (1, 'Rent', 'HOUSE')"
    )

    @BeforeEach
    fun setUp() {
        initialSqlQueries.forEach(jdbc::update)
    }

    @AfterEach
    fun tearDown() {
        jdbc.execute("delete from plan_entries")
        jdbc.execute("delete from plans")
        jdbc.execute("alter table plans alter column id restart with 1")
        jdbc.execute("delete from categories")
        jdbc.execute("delete from category_sets")
        jdbc.execute("alter table categories alter column id restart with 1")
    }

    @Test
    fun `should get current plans`() {
        // when
        val plans = adapter.getCurrentPlans(ownerIdOf(1)).getOrNull()

        // then
        assertThat(plans).containsExactly(
            PlanDefinition.existing(
                id = 2.toPlanId(),
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
                id = 3.toPlanId(),
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
                id = 1.toPlanId(),
                name = "June 2023",
                start = LocalDateTime.parse("2023-06-01T00:00:00"),
                end = LocalDateTime.parse("2023-06-30T23:59:59")
            ),
        )
    }

    @Test
    fun `should save the new plan`() {
        // given
        val plan = Plan(
            version = versionOf(1),
            ownerId = ownerIdOf(1),
            definition = PlanDefinition.existing(
                id = PlanId.UNSETTLED,
                name = "July 2023",
                start = LocalDateTime.parse("2023-07-01T00:00:00"),
                end = LocalDateTime.parse("2023-07-31T23:59:59")
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
        assertThat(result?.id).isEqualTo(4.toPlanId())
        assertThat(result?.entries).contains(
            PlannedCategory(
                category = Category.of(1, "Rent", Icon.HOUSE),
                value = Money.of(317.98, "PLN")
            )
        )
        assertThat(result?.name).isEqualTo("July 2023")
        assertThat(result?.start).isEqualTo(LocalDateTime.parse("2023-07-01T00:00:00"))
        assertThat(result?.end).isEqualTo(LocalDateTime.parse("2023-07-31T23:59:59"))
        assertThat(result?.entries).contains(
            PlannedCategory(
                category = Category.of(1, "Rent", Icon.HOUSE),
                value = Money.of(317.98, "PLN")
            )
        )
    }
}