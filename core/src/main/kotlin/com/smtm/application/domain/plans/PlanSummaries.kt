package com.smtm.application.domain.plans

import com.smtm.application.domain.Aggregate
import com.smtm.application.domain.OwnerId
import com.smtm.application.domain.Version
import java.time.Clock
import java.time.LocalDateTime

data class PlanSummaries(
    override val id: OwnerId,
    override val version: Version,
    val current: List<PlanSummary>
) : Aggregate<OwnerId> {

    fun getActivePlans(clock: Clock): List<PlanSummary> {
        return current
            .filter { it.period.contains(LocalDateTime.now(clock)) }
    }

    fun getFuturePlans(clock: Clock): List<PlanSummary> {
        return current
            .filter { it.period.start > LocalDateTime.now(clock) }
    }

    fun getPastPlans(clock: Clock): List<PlanSummary> {
        return current
            .filter { it.period.endInclusive < LocalDateTime.now(clock) }
    }
}

fun fetchedPlanSummariesOf(id: OwnerId, version: Version, plans: List<PlanSummary>) = PlanSummaries(
    id = id,
    version = version,
    current = plans
)