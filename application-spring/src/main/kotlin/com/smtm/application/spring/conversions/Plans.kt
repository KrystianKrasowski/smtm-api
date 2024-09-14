package com.smtm.application.spring.conversions

import com.smtm.api.HalCollection
import com.smtm.api.LinkFactory
import com.smtm.api.v1.DatePeriodDto
import com.smtm.api.v1.PlanHeaderDto
import com.smtm.api.v1.PlanHeaderResource
import com.smtm.application.spring.endpoints.PlanHeadersEndpoint
import com.smtm.application.spring.endpoints.PlansEndpoint
import com.smtm.core.api.PlanHeaders
import com.smtm.core.domain.plans.PlanHeader

object Plans {

    fun PlanHeaders.toHalCollection(linkFactory: LinkFactory): HalCollection =
        HalCollection(
            links = mapOf(
                "self" to linkFactory.create(PlanHeadersEndpoint.PATH)
            ),
            count = size,
            total = size,
            embedded = mapOf(
                "plan-headers" to map { it.toResource(linkFactory) }
            )
        )

    private fun PlanHeader.toResource(linkFactory: LinkFactory): PlanHeaderResource =
        PlanHeaderResource(
            links = mapOf(
                "self" to linkFactory.create("${PlansEndpoint.PATH}/$id")
            ),
            id = id.value,
            header = PlanHeaderDto(
                name = name,
                period = DatePeriodDto(
                    start = period.start,
                    end = period.endInclusive
                )
            ),
        )
}
