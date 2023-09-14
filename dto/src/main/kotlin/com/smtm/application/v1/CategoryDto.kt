package com.smtm.application.v1

import com.fasterxml.jackson.annotation.JsonProperty
import com.smtm.application.HalResource
import com.smtm.application.Link

data class CategoryDto(
    @JsonProperty("_links") override val links: Map<String, Link> = emptyMap(),
    @JsonProperty("id") val id: Long? = null,
    @JsonProperty("name") val name: String,
    @JsonProperty("icon") val icon: String
) : HalResource(links)
