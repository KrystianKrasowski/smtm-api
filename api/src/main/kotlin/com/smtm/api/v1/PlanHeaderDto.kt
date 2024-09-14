package com.smtm.api.v1

import com.fasterxml.jackson.annotation.JsonProperty

data class PlanHeaderDto(
    @JsonProperty("name") val name: String,
    @JsonProperty("period") val period: DatePeriodDto
)
