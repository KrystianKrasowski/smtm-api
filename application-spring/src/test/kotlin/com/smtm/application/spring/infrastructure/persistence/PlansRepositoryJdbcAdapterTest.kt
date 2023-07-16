package com.smtm.application.spring.infrastructure.persistence

import com.smtm.application.domain.ownerIdOf
import com.smtm.application.domain.plans.existingPlanSummaryOf
import com.smtm.application.domain.versionOf
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcOperations
import java.time.Clock
import java.time.LocalDateTime

@SpringBootTest
class PlansRepositoryJdbcAdapterTest {

    @Autowired
    private lateinit var jdbc: JdbcOperations

    private val clock = Clock.systemUTC()

    private val adapter get() = PlansRepositoryJdbcAdapter(clock, jdbc)

    private val initialSqlQueries = listOf(
        "insert into plan_sets (owner_id, version, last_modified) values (1, 1, timestamp '2023-07-05 12:48:56')",
        "insert into plans (owner_id, version, name, start, \"end\") values (1, 1, 'June 2023', timestamp '2023-06-01 00:00:00', timestamp '2023-06-30 23:59:59')",
        "insert into plans (owner_id, version, name, start, \"end\") values (1, 1, 'July 2023', timestamp '2023-07-01 00:00:00', timestamp '2023-07-31 23:59:59')",
        "insert into plans (owner_id, version, name, start, \"end\") values (1, 1, 'August 2023', timestamp '2023-08-01 00:00:00', timestamp '2023-08-31 23:59:59')",
    )

    @BeforeEach
    fun setUp() {
        initialSqlQueries.forEach(jdbc::update)
    }

    @AfterEach
    fun tearDown() {
        jdbc.execute("delete from plans")
        jdbc.execute("delete from plan_sets")
        jdbc.execute("alter table plans alter column id restart with 1")
    }

    @Test
    fun `should get plan summaries`() {
        // when
        val plans = adapter.getAllPlanSummaries(ownerIdOf(1)).getOrNull()

        // then
        assertThat(plans?.id).isEqualTo(ownerIdOf(1))
        assertThat(plans?.version).isEqualTo(versionOf(1))
        assertThat(plans?.allPlans).containsExactlyInAnyOrder(
            existingPlanSummaryOf(
                id = 2,
                name = "July 2023",
                start = LocalDateTime.parse("2023-07-01T00:00:00"),
                end = LocalDateTime.parse("2023-07-31T23:59:59")
            ),
            existingPlanSummaryOf(
                id = 3,
                name = "August 2023",
                start = LocalDateTime.parse("2023-08-01T00:00:00"),
                end = LocalDateTime.parse("2023-08-31T23:59:59")
            ),
            existingPlanSummaryOf(
                id = 1,
                name = "June 2023",
                start = LocalDateTime.parse("2023-06-01T00:00:00"),
                end = LocalDateTime.parse("2023-06-30T23:59:59")
            ),
        )
    }
}