package com.smtm.application.domain.plans

import com.smtm.application.domain.ownerIdOf
import com.smtm.application.domain.versionOf
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class PlanSummariesTest {

    private val clock = Clock.fixed(Instant.parse("2023-07-16T15:48:35.000Z"), ZoneId.of("UTC"))

    private val planSummariesPrototype = fetchedPlanSummariesOf(
        id = ownerIdOf(1),
        version = versionOf(4),
        plans = listOf(
            existingPlanSummaryOf(
                id = 1,
                name = "May",
                start = LocalDateTime.parse("2023-05-01T00:00:00"),
                end = LocalDateTime.parse("2023-05-31T23:59:59")
            ),
            existingPlanSummaryOf(
                id = 2,
                name = "June",
                start = LocalDateTime.parse("2023-06-01T00:00:00"),
                end = LocalDateTime.parse("2023-06-30T23:59:59")
            ),
            existingPlanSummaryOf(
                id = 3,
                name = "July",
                start = LocalDateTime.parse("2023-07-01T00:00:00"),
                end = LocalDateTime.parse("2023-07-31T23:59:59")
            ),
            existingPlanSummaryOf(
                id = 4,
                name = "August",
                start = LocalDateTime.parse("2023-08-01T00:00:00"),
                end = LocalDateTime.parse("2023-08-31T23:59:59")
            ),
        )
    )

    @Test
    fun `should return active plans`() {
        // when
        val activePlans = planSummariesPrototype.getActivePlans(clock)

        // then
        assertThat(activePlans).contains(
            existingPlanSummaryOf(
                id = 3,
                name = "July",
                start = LocalDateTime.parse("2023-07-01T00:00:00"),
                end = LocalDateTime.parse("2023-07-31T23:59:59")
            )
        )
    }

    @Test
    fun `should return future plans`() {
        // when
        val futurePlans = planSummariesPrototype.getFuturePlans(clock)

        // then
        assertThat(futurePlans).contains(
            existingPlanSummaryOf(
                id = 4,
                name = "August",
                start = LocalDateTime.parse("2023-08-01T00:00:00"),
                end = LocalDateTime.parse("2023-08-31T23:59:59")
            )
        )
    }

    @Test
    fun `should return past plans`() {
        // when
        val pastPlans = planSummariesPrototype.getPastPlans(clock)

        // then
        assertThat(pastPlans).contains(
            existingPlanSummaryOf(
                id = 1,
                name = "May",
                start = LocalDateTime.parse("2023-05-01T00:00:00"),
                end = LocalDateTime.parse("2023-05-31T23:59:59")
            ),
            existingPlanSummaryOf(
                id = 2,
                name = "June",
                start = LocalDateTime.parse("2023-06-01T00:00:00"),
                end = LocalDateTime.parse("2023-06-30T23:59:59")
            )
        )
    }
}