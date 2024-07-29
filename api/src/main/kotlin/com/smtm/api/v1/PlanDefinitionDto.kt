package com.smtm.api.v1

import com.fasterxml.jackson.annotation.JsonProperty
import com.smtm.api.HalResource
import com.smtm.api.Link

data class PlanDefinitionDto(
    @JsonProperty("_links") override val links: Map<String, Link>,
    @JsonProperty("id") val id: Long,
    @JsonProperty("name") val name: String,
    @JsonProperty("period") val period: PeriodDto
) : HalResource(links)
