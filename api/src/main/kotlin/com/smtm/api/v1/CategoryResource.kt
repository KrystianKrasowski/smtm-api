package com.smtm.api.v1

import com.fasterxml.jackson.annotation.JsonProperty
import com.smtm.api.HalResource
import com.smtm.api.Link

data class CategoryResource(
    @JsonProperty("_links") override val links: Map<String, Link>,
    @JsonProperty("id") override val id: Long,
    private val category: CategoryDto
) : HalResource<Long>(links, id) {

    @JsonProperty("name") val name: String = category.name
    @JsonProperty("icon") val icon: String = category.icon
}
