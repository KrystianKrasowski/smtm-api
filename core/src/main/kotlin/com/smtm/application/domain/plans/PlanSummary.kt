package com.smtm.application.domain.plans

import java.time.LocalDateTime

data class PlanSummary(
    val id: Long,
    val name: String,
    val period: ClosedRange<LocalDateTime>,
    val status: PlanStatus
)

fun existingPlanSummaryOf(id: Long, name: String, start: LocalDateTime, end: LocalDateTime) = PlanSummary(
    id, name, start..end, PlanStatus.ACTIVE
)