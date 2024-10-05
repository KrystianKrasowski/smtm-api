package com.smtm.core.domain.tags

import com.smtm.core.domain.EntityId
import com.smtm.core.domain.Icon

internal interface Tag {

    val id: EntityId
    val name: String
    val icon: Icon
}
