package com.smtm.application.domain.plans

import arrow.core.Either
import arrow.core.right
import com.smtm.application.domain.NumericId
import java.time.LocalDateTime

data class PlanDefinition(
    val id: NumericId,
    val name: String,
    val period: ClosedRange<LocalDateTime>
) {

    fun validate(): Either<PlansProblem.Violations, PlanDefinition> =
        right()

    companion object {

        val EMPTY = PlanDefinition(
            id = NumericId.UNSETTLED,
            name = "",
            period = LocalDateTime.parse("1970-01-01T00:00:00")..LocalDateTime.parse("1970-01-01T23:59:59")
        )

        fun existing(id: NumericId, name: String, start: LocalDateTime, end: LocalDateTime) = PlanDefinition(
            id = id,
            name = name,
            period = start..end
        )
    }
}
