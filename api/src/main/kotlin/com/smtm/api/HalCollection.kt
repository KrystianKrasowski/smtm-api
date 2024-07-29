package com.smtm.api

import com.fasterxml.jackson.annotation.JsonProperty

data class HalCollection<T : HalResource>(
    @JsonProperty("_links") val links: Map<String, Link>,
    @JsonProperty("count") val count: Int,
    @JsonProperty("total") val total: Int,
    @JsonProperty("_embedded") val embedded: Map<String, Iterable<T>>
)

fun <T: HalResource> Collection<T>.toHalCollection(links: Map<String, Link>, name: String) = HalCollection(
    links = links,
    count = size,
    total = size,
    embedded = mapOf(
        name to this
    )
)
