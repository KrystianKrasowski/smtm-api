package com.smtm.api.v1

import com.fasterxml.jackson.annotation.JsonProperty
import com.smtm.api.HalResource
import com.smtm.api.Link

data class PlanResource(
    @JsonProperty("_links") override val links: Map<String, Link>,
    @JsonProperty("id") override val id: String,
    @JsonProperty("_embedded") override val embedded: Map<String, Collection<CategoryResource>>,
    private val plan: PlanDto
) : HalResource<String>(links, id, embedded) {

    @JsonProperty("name") val name: String = plan.name
    @JsonProperty("period") val period: DatePeriodDto = plan.period
    @JsonProperty("entries") val entries: List<PlanDto.Entry> = plan.entries
}
