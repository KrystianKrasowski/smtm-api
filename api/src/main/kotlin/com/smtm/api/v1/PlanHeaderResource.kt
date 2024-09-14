package com.smtm.api.v1

import com.fasterxml.jackson.annotation.JsonProperty
import com.smtm.api.HalResource
import com.smtm.api.Link

data class PlanHeaderResource(
    @JsonProperty("_links") override val links: Map<String, Link>,
    @JsonProperty("id") override val id: Long,
    private val header: PlanHeaderDto
): HalResource<Long>(links, id) {

    @JsonProperty("name") val name: String = header.name
    @JsonProperty("period") val period: DatePeriodDto = header.period
}
