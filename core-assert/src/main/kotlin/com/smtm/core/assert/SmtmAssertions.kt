package com.smtm.core.assert

import com.smtm.core.domain.plans.Plan

object SmtmAssertions {

    fun assertThat(plan: Plan?): PlanAssert =
        PlanAssert(plan)
}
