package com.smtm.core.domain.wallets

import com.smtm.core.domain.EntityId
import com.smtm.core.domain.Icon

data class Wallet(val id: EntityId, val name: String, val icon: Icon) {

    companion object {

        fun of(id: EntityId, name: String, icon: Icon): Wallet =
            Wallet(id, name, icon)

        fun newOf(name: String, icon: Icon) =
            of(EntityId.generate("wallet"), name, icon)
    }
}
