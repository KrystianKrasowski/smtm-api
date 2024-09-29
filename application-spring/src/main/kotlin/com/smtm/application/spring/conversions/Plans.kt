package com.smtm.application.spring.conversions

import com.smtm.api.LinkFactory
import com.smtm.api.ResourcePaths
import com.smtm.api.v1.DatePeriodDto
import com.smtm.api.v1.MoneyDto
import com.smtm.api.v1.PlanDto
import com.smtm.api.v1.PlanHeaderDto
import com.smtm.api.v1.PlanHeaderResource
import com.smtm.api.v1.PlanHeadersCollection
import com.smtm.api.v1.PlanResource
import com.smtm.application.spring.conversions.Categories.toResource
import com.smtm.core.api.PlanHeaders
import com.smtm.core.domain.EntityId
import com.smtm.core.domain.categories.Category
import com.smtm.core.domain.plans.Plan
import com.smtm.core.domain.plans.PlanHeader
import javax.swing.text.html.parser.Entity

object Plans {

    fun PlanHeaders.toHalCollection(linkFactory: LinkFactory): PlanHeadersCollection =
        PlanHeadersCollection(
            links = mapOf(
                "self" to linkFactory.create(ResourcePaths.PLAN_HEADERS)
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
                "self" to linkFactory.create("${ResourcePaths.PLANS}/$id")
            ),
            id = id.asString(),
            plan = PlanDto(
                name = name,
                period = DatePeriodDto(
                    start = start,
                    end = end
                ),
                entries = entries.map { it.toEntryDto(categories) },
            ),
            embedded = mapOf(
                "categories" to categories.map { it.toResource(linkFactory) }
            )
        )

    fun PlanDto.toHeader(id: String? = null): PlanHeader =
        id?.let { EntityId.of(it) }
            ?.let { PlanHeader.of(it, name, period.start, period.end) }
            ?: PlanHeader.newOf(name, period.start, period.end)

    fun PlanDto.toEntries(): List<Plan.Entry> =
        entries.map {
            Plan.Entry(
                categoryId = EntityId.of(it.categoryId),
                value = it.value.monetaryAmount
            )
        }

    private fun PlanHeader.toResource(linkFactory: LinkFactory): PlanHeaderResource =
        PlanHeaderResource(
            links = mapOf(
                "self" to linkFactory.create("${ResourcePaths.PLANS}/$id")
            ),
            id = id.asString(),
            header = PlanHeaderDto(
                name = name,
                period = DatePeriodDto(
                    start = period.start,
                    end = period.endInclusive
                )
            ),
        )

    private fun Plan.Entry.toEntryDto(categories: List<Category>): PlanDto.Entry =
        PlanDto.Entry(
            categoryId = categories.first { it.id == categoryId }.id.asString(),
            value = MoneyDto.of(value)
        )
}
