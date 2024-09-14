package com.smtm.core.domain.plans

import com.smtm.core.domain.NumericId
import java.time.LocalDate

data class PlanHeader(
    val id: NumericId,
    val name: String,
    val period: ClosedRange<LocalDate>
)
