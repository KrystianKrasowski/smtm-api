package com.smtm.core.domain.plans

import com.smtm.core.domain.EntityId
import io.cucumber.java.DataTableType
import java.time.LocalDate

class ParameterTypes {

    @DataTableType
    fun planHeader(entry: Map<String, String>): PlanHeader =
        PlanHeader.of(
            id = EntityId.of(entry.getValue("id")),
            name = entry.getOrDefault("name", ""),
            start = LocalDate.parse(entry.getValue("start")),
            end = LocalDate.parse(entry.getValue("end")),
        )
}
