package com.smtm.transactions

import com.smtm.transactions.api.categoryOf
import io.cucumber.java.DataTableType

class ParameterTypes {

    @DataTableType
    fun category(input: Map<String, String>) = categoryOf(
        id = input["id"]?.toLong(),
        name = input["name"] ?: error("Category name is not defined in data table"),
        icon = input["icon"] ?: error("Category icon is not defined in data table"),
        parent = input["parent"]?.toLong()
    )
}
