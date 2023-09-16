package com.smtm.application.domain.plans

data class PlanRequest(val definition: PlanDefinition, val categories: List<PlannedCategory>)
