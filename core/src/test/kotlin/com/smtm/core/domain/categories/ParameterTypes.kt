package com.smtm.core.domain.categories

import arrow.core.getOrElse
import com.smtm.core.World
import com.smtm.core.domain.EntityId
import com.smtm.core.domain.Icon
import io.cucumber.java.DataTableType
import io.cucumber.java.ParameterType

class ParameterTypes(private val world: World) {

    @DataTableType
    fun category(entry: Map<String, String>): Category =
        Category.of(
            id = EntityId.of(entry.getValue("id")),
            name = entry["name"] ?: "",
            icon = entry.extractIcon()
        )

    @ParameterType("([A-Za-z0-9]+)")
    fun categoryByName(input: String): Category =
        world.categoriesRepository
            .getCategories()
            .map { it.getByName(input) }
            .getOrElse { error("Cannot get category by name") }

    private fun Map<String, String>.extractIcon() = getValue("icon")
        .let { Icon.valueOf(it) }
}
