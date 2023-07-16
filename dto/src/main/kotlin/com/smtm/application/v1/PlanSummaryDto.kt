package com.smtm.application.v1

import com.fasterxml.jackson.annotation.JsonProperty
import com.smtm.application.HalResource
import com.smtm.application.Link

data class PlanSummaryDto(
    @JsonProperty("_links") override val links: Map<String, Link>,
    @JsonProperty("id") val id: Long,
    @JsonProperty("name") val name: String,
    @JsonProperty("period") val period: PeriodDto,
    @JsonProperty("status") val status: PlanStatusDto
) : HalResource(links)