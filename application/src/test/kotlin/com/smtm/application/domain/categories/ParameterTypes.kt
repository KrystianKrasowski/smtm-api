package com.smtm.application.domain.categories

import com.smtm.application.domain.Icon
import io.cucumber.java.DataTableType

class ParameterTypes {

    @DataTableType
    fun category(entry: Map<String, String>): Category {
        return categoryOf(
            id = entry.extractId(),
            name = entry["name"] ?: "",
            icon = entry.extractIcon(),
            status = entry.extractStatus()
        )
    }

    private fun Map<String, String>.extractId() = get("id")
        ?.takeIf { it.isNotBlank() }
        ?.toLong()

    private fun Map<String, String>.extractIcon() = getValue("icon")
        .let { Icon.valueOf(it) }

    private fun Map<String, String>.extractStatus() = get("status")
        ?.let { Category.Status.valueOf(it) }
        ?: Category.Status.EXISTING
}