package com.smtm.application.spring.conversions

import com.smtm.application.LinkFactory
import com.smtm.application.domain.NumericId
import com.smtm.application.domain.plans.Plan
import com.smtm.application.domain.plans.PlanDefinition
import com.smtm.application.domain.plans.PlannedCategory
import com.smtm.application.spring.conversions.Categories.toDomain
import com.smtm.application.spring.conversions.Categories.toDto
import com.smtm.application.spring.resources.PlansResource
import com.smtm.application.v1.MoneyDto
import com.smtm.application.v1.PeriodDto
import com.smtm.application.v1.PlanDto

object Plans {

    fun PlanDto.toPlanDefinition(): PlanDefinition =
        PlanDefinition(
            id = NumericId.of(id),
            name = name,
            period = period.start..period.end
        )

    fun PlanDto.toPlannedCategories(): List<PlannedCategory> =
        entries
            .map { PlannedCategory(it.category.toDomain(), it.value.monetaryAmount) }

    fun Plan.toDto(linksFactory: LinkFactory): PlanDto =
        PlanDto(
            links = mapOf(
                "self" to linksFactory.create("${PlansResource.PATH}/${id.value}"),
            ),
            id = id.value,
            name = name,
            period = PeriodDto(
                start = start,
                end = end
            ),
            entries = entries.map { it.toDto(linksFactory) }
        )

    private fun PlannedCategory.toDto(linksFactory: LinkFactory) =
        PlanDto.Entry(
            category = category.toDto(linksFactory),
            value = MoneyDto.of(value)
        )
}
