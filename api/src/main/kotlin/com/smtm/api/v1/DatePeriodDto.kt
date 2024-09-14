package com.smtm.api.v1

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

data class DatePeriodDto(
    @JsonProperty("start") val start: LocalDate,
    @JsonProperty("end") val end: LocalDate
)
