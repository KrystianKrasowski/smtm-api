package com.smtm.application.domain.categories

import com.smtm.application.domain.Icon
import com.smtm.application.domain.NumericId
import io.cucumber.java.DataTableType

class ParameterTypes {

    @DataTableType
    fun category(entry: Map<String, String>): Category =
        Category.of(
            id = NumericId.of(id = entry.extractId()),
            name = entry["name"] ?: "",
            icon = entry.extractIcon()
        )

    private fun Map<String, String>.extractId() = get("id")
        ?.takeIf { it.isNotBlank() }
        ?.toLong()

    private fun Map<String, String>.extractIcon() = getValue("icon")
        .let { Icon.valueOf(it) }
}