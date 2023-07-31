package com.smtm.application.v1

import com.smtm.application.HalResource
import com.smtm.application.Link

data class PlanDto(
    override val links: Map<String, Link> = emptyMap(),
    val id: Long,
    val name: String,
    val period: PeriodDto,
    val entries: List<Entry>
) : HalResource(links) {

    data class Entry(val category: CategoryDto, val value: MoneyDto)
}

data class NewPlanDto(
    val name: String,
    val period: PeriodDto,
    val entries: List<Entry>
) {

    data class Entry(val categoryId: Long, val value: MoneyDto)
}
