package com.smtm.api

import java.net.URI

abstract class HalResource<ID>(
    open val links: Map<String, Link> = emptyMap(),
    open val id: ID,
    open val embedded: Map<String, Collection<HalResource<out Any>>>? = null
) {

    fun getSelfURI(): URI =
        URI.create(links.getValue("self").href)
}
