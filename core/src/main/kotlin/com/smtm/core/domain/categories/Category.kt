package com.smtm.core.domain.categories

import com.smtm.core.domain.EntityId
import com.smtm.core.domain.Icon

data class Category(val id: EntityId, val name: String, val icon: Icon) {

    companion object {

        fun of(id: EntityId, name: String, icon: Icon): Category =
            Category(id, name, icon)

        fun newOf(name: String, icon: Icon) =
            of(EntityId.generate("category"), name, icon)
    }
}
