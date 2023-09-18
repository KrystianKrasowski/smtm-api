package com.smtm.application.assert

import com.smtm.application.domain.plans.Plan

object SmtmAssertions {

    fun assertThat(plan: Plan?): PlanAssert =
        PlanAssert(plan)
}
