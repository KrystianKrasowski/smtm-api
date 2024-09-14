package com.smtm.infrastructure.adapters

import arrow.core.getOrElse
import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.containsExactly
import com.smtm.core.api.PlansQueries
import com.smtm.core.domain.NumericId
import com.smtm.core.domain.OwnerId
import com.smtm.core.domain.plans.PlanHeader
import com.smtm.infrastructure.persistence.TestDatabase
import com.smtm.infrastructure.persistence.runSql
import javax.sql.DataSource
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.time.LocalDate

class PlanQueriesAdapterTest {

    private val dataSource: DataSource = TestDatabase.setup()
    private val planQueriesAdapter get() = PlanQueriesAdapter(dataSource)

    @BeforeEach
    fun setUp() {
        dataSource.runSql("""
            INSERT INTO plans (owner_id, version, name, start, "end") 
            VALUES 
            (1, 1, 'August 2024', '2024-08-01', '2024-08-31'),
            (1, 1, 'September 2024', '2024-09-01', '2024-09-30'),
            (1, 1, 'October 2024', '2024-10-01', '2024-10-31')
        """.trimIndent())
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
        val result = planQueriesAdapter
            .getPlanHeadersBy(criteria)
            .getOrElse { throw IllegalStateException("Expected to return success here", it) }

        assertThat(result).contains(planDefinitionOf(2, "September 2024", "2024-09-01", "2024-09-30"))
    }

    @Test
    fun `should return all owner's plans`() {
        // when
        val criteria = PlansQueries.Criteria.by(
            owner = OwnerId(1)
        )
        val result = planQueriesAdapter
            .getPlanHeadersBy(criteria)
            .getOrElse { throw IllegalStateException("Expected to return success here", it) }

        assertThat(result).containsExactly(
            planDefinitionOf(3, "October 2024", "2024-10-01", "2024-10-31"),
            planDefinitionOf(2, "September 2024", "2024-09-01", "2024-09-30"),
            planDefinitionOf(1, "August 2024", "2024-08-01", "2024-08-31")
        )
    }
}

private fun planDefinitionOf(id: Long, name: String, start: String, end: String): PlanHeader =
    PlanHeader(
        id = NumericId.of(id),
        name = name,
        period = LocalDate.parse(start)..LocalDate.parse(end),
    )
