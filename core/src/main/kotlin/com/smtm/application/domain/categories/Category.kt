package com.smtm.application.domain.categories

import com.smtm.application.domain.Icon
import com.smtm.application.domain.NumericId

data class Category(val id: NumericId, val name: String, val icon: Icon) {

    companion object {

        fun of(id: NumericId, name: String, icon: Icon) =
            Category(id, name, icon)

        fun of(id: Long, name: String, icon: Icon) =
            of(NumericId.of(id), name, icon)

        fun newOf(name: String, icon: Icon) =
            of(NumericId.UNSETTLED, name, icon)
    }
}