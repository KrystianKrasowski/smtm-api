package com.smtm.api.v1

import com.fasterxml.jackson.annotation.JsonProperty

data class PlanDto(
    @JsonProperty("entries") val entries: List<Entry>,
    private val header: PlanHeaderDto
) {

    @JsonProperty("name") val name: String = header.name
    @JsonProperty("period") val period: DatePeriodDto = header.period

    data class Entry(
        @JsonProperty("category-id") val categoryId: Long,
        @JsonProperty("value") val value: MoneyDto
    )
}
