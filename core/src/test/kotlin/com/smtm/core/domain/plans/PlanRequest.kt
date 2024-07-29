package com.smtm.core.domain.plans

data class PlanRequest(val definition: PlanDefinition, val categories: List<PlannedCategory>)
