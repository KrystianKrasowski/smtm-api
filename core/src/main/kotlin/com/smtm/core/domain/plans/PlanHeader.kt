package com.smtm.core.domain.plans

import com.smtm.core.domain.EntityId
import java.time.LocalDate

data class PlanHeader(
    val id: EntityId,
    val name: String,
    val period: ClosedRange<LocalDate>
)
