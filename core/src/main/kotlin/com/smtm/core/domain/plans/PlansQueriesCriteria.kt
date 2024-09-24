package com.smtm.core.domain.plans

import com.smtm.core.api.PlansQueries
import java.time.LocalDate

internal data class PlansQueriesCriteria(
    override val byDateWithinPeriod: LocalDate?
) : PlansQueries.Criteria
