package com.smtm.core.domain.plans

import com.smtm.core.api.PlansQueries
import com.smtm.core.domain.OwnerId
import java.time.LocalDate

internal data class PlansQueriesCriteria(
    override val byOwner: OwnerId,
    override val byDateWithinPeriod: LocalDate?
) : PlansQueries.Criteria
