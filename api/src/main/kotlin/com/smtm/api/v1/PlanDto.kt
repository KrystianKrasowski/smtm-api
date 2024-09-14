package com.smtm.api.v1

import com.fasterxml.jackson.annotation.JsonProperty

data class PlanDto(
    @JsonProperty("entries") val entries: List<Entry>,
    private val planHeaderDto: PlanHeaderDto
) {

    @JsonProperty("name") val name: String = planHeaderDto.name
    @JsonProperty("period") val period: DatePeriodDto = planHeaderDto.period

    data class Entry(
        @JsonProperty("category-id") val categoryId: Long,
        @JsonProperty("value") val value: MoneyDto
    )
}
