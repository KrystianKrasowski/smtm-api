package com.smtm.application.domain.categories

import com.smtm.application.domain.Icon
import io.cucumber.java.DataTableType

class ParameterTypes {

    @DataTableType
    fun category(entry: Map<String, String>): Category {
        return Category(
            id = entry["id"]?.toLong(),
            name = entry["name"] ?: "",
            icon = Icon.valueOf(entry.getValue("icon"))
        )
    }
}