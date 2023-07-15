package com.smtm.application.domain.plans

import com.smtm.application.domain.Aggregate
import com.smtm.application.domain.OwnerId
import com.smtm.application.domain.Version

data class PlanSummaries(
    override val id: OwnerId,
    override val version: Version,
    val current: List<PlanSummary>
) : Aggregate<OwnerId>

fun fetchedPlanSummariesOf(id: OwnerId, version: Version, plans: List<PlanSummary>) = PlanSummaries(
    id = id,
    version = version,
    current = plans
)