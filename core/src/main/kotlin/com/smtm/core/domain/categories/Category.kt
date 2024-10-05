package com.smtm.core.domain.categories

import com.smtm.core.domain.EntityId
import com.smtm.core.domain.Icon
import com.smtm.core.domain.tags.Tag

data class Category(
    override val id: EntityId,
    override val name: String,
    override val icon: Icon
) : Tag {

    companion object {

        fun of(id: EntityId, name: String, icon: Icon): Category =
            Category(id, name, icon)

        fun newOf(name: String, icon: Icon) =
            of(EntityId.generate("category"), name, icon)
    }
}
