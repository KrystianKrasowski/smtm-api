package com.smtm.api.v1

import com.fasterxml.jackson.annotation.JsonProperty

data class PlanDto(
    @JsonProperty("name") val name: String,
    @JsonProperty("period") val period: DatePeriodDto,
    @JsonProperty("entries") val entries: List<Entry>
) {

    data class Entry(
        @JsonProperty("category-id") val categoryId: String,
        @JsonProperty("value") val value: MoneyDto
    )
}
