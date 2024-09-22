package com.smtm.core.domain.categories

import com.smtm.core.domain.OwnerId
import com.smtm.core.domain.Version

data class Categories(
    val id: OwnerId,
    val version: Version,
    private val actual: List<Category>
) : Iterable<Category> {

    val size: Int =
        actual.size

    override fun iterator(): Iterator<Category> {
        return actual.iterator()
    }

    companion object {

        fun empty(ownerId: OwnerId): Categories =
            Categories(ownerId, Version.of(0), emptyList())
    }
}
