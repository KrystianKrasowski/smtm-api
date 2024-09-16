package com.smtm.application.spring.conversions

import com.smtm.api.HalCollection
import com.smtm.api.LinkFactory
import com.smtm.api.v1.CategoryDto
import com.smtm.api.v1.CategoryResource
import com.smtm.api.v1.DatePeriodDto
import com.smtm.api.v1.MoneyDto
import com.smtm.api.v1.PlanDto
import com.smtm.api.v1.PlanHeaderDto
import com.smtm.api.v1.PlanHeaderResource
import com.smtm.api.v1.PlanResource
import com.smtm.application.spring.endpoints.CategoriesEndpoint
import com.smtm.application.spring.endpoints.PlanHeadersEndpoint
import com.smtm.application.spring.endpoints.PlansEndpoint
import com.smtm.core.api.PlanHeaders
import com.smtm.core.domain.plans.Plan
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

    fun Plan.toHalResource(linkFactory: LinkFactory): PlanResource =
        PlanResource(
            links = mapOf(
                "self" to linkFactory.create("${PlansEndpoint.PATH}/${id.value}")
            ),
            id = id.value,
            plan = PlanDto(
                name = name,
                period = DatePeriodDto(
                    start = start,
                    end = end
                ),
                entries = entries.map { it.toEntryDto() },
            ),
            embedded = mapOf(
                "categories" to entries.map { it.toCategoryResource(linkFactory) }
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

    private fun Plan.Entry.toEntryDto(): PlanDto.Entry =
        PlanDto.Entry(
            categoryId = category.id.value,
            value = MoneyDto.of(value)
        )

    private fun Plan.Entry.toCategoryResource(linkFactory: LinkFactory): CategoryResource =
        CategoryResource(
            links = mapOf(
                "self" to linkFactory.create("${CategoriesEndpoint.PATH}/${category.id.value}")
            ),
            id = category.id.value,
            category = CategoryDto(
                name = category.name,
                icon = category.icon.name
            )
        )
}
