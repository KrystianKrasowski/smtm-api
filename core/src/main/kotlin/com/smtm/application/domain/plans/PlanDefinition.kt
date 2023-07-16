package com.smtm.application.domain.plans

import java.time.LocalDateTime

data class PlanDefinition(
    val id: Long,
    val name: String,
    val period: ClosedRange<LocalDateTime>
)

fun existingPlanDefinitionOf(id: Long, name: String, start: LocalDateTime, end: LocalDateTime) = PlanDefinition(
    id, name, start..end
)