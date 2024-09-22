package com.smtm.api

import com.fasterxml.jackson.annotation.JsonProperty

data class HalCollection(
    @JsonProperty("_links") val links: Map<String, Link>,
    @JsonProperty("count") val count: Int,
    @JsonProperty("total") val total: Int,
    @JsonProperty("_embedded") val embedded: Map<String, Iterable<HalResource<out Any>>>
)

