package com.smtm.core.domain.wallets

import com.smtm.core.domain.EntityId
import com.smtm.core.domain.Icon
import io.cucumber.java.DataTableType

class ParameterTypes {

    @DataTableType
    fun category(entry: Map<String, String>): Wallet =
        Wallet.of(
            id = EntityId.of(entry.getValue("id")),
            name = entry["name"] ?: "",
            icon = entry.extractIcon()
        )

    private fun Map<String, String>.extractIcon() = getValue("icon")
        .let { Icon.valueOf(it) }
}
