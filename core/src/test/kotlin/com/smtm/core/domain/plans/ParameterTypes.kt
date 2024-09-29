package com.smtm.core.domain.plans

import com.smtm.core.World
import com.smtm.core.domain.EntityId
import io.cucumber.java.DataTableType
import org.javamoney.moneta.Money
import java.time.LocalDate

class ParameterTypes(private val world: World) {

    @DataTableType
    fun planHeader(entry: Map<String, String>): PlanHeader =
        PlanHeader.of(
            id = EntityId.of(entry.getValue("id")),
            name = entry.getOrDefault("name", ""),
            start = LocalDate.parse(entry.getValue("start")),
            end = LocalDate.parse(entry.getValue("end")),
        )

    @DataTableType
    fun planEntry(input: Map<String, String>): Plan.Entry =
        Plan.entry(
            categoryId = getCategoryIdByNameOrUnknown(input.getValue("category")),
            value = Money.parse(input.getValue("value")),
        )

    private fun getCategoryIdByNameOrUnknown(name: String): EntityId =
        world.categoriesRepository.categoryList
            .firstOrNull { it.name == name }
            ?.id
            ?: EntityId.generate("unknown")
}
