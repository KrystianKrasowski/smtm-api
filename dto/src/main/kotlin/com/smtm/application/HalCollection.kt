package com.smtm.application

import com.fasterxml.jackson.annotation.JsonProperty

data class HalCollection<T : HalResource>(
    @JsonProperty("_links") val links: Map<String, Link>,
    @JsonProperty("count") val count: Int,
    @JsonProperty("total") val total: Int,
    @JsonProperty("_embedded") val embedded: Map<String, Iterable<T>>
)