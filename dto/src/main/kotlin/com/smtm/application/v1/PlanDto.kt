package com.smtm.application.v1

import com.fasterxml.jackson.annotation.JsonProperty
import com.smtm.application.HalResource
import com.smtm.application.Link

data class PlanDto(
    @JsonProperty("_links") override val links: Map<String, Link> = emptyMap(),
    @JsonProperty("id") val id: Long? = null,
    @JsonProperty("name") val name: String,
    @JsonProperty("period") val period: PeriodDto,
    @JsonProperty("entries") val entries: List<Entry>
) : HalResource(links) {

    data class Entry(
        @JsonProperty("category") val category: CategoryDto,
        @JsonProperty("value") val value: MoneyDto
    )
}
