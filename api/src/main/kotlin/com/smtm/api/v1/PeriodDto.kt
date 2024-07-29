package com.smtm.api.v1

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class PeriodDto(
    @JsonProperty("start") val start: LocalDateTime,
    @JsonProperty("end") val end: LocalDateTime
)
