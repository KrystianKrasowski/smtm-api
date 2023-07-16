package com.smtm.application.domain.plans

import com.smtm.application.domain.Aggregate
import com.smtm.application.domain.OwnerId
import com.smtm.application.domain.Version
import java.time.Clock
import java.time.LocalDateTime

data class PlanSummaries internal constructor(
    override val id: OwnerId,
    override val version: Version,
    val allPlans: List<PlanSummary>,
    private val clock: Clock
) : Aggregate<OwnerId> {

    private val now = LocalDateTime.now(clock)

    val activePlans = allPlans.filter { it.period.contains(now) }
    val futurePlans = allPlans.filter { it.period.start > now }
    val pastPlans = allPlans.filter { it.period.endInclusive < now }
}

fun fetchedPlanSummariesOf(clock: Clock, id: OwnerId, version: Version, plans: List<PlanSummary>) = PlanSummaries(
    id = id,
    version = version,
    allPlans = plans,
    clock = clock
)