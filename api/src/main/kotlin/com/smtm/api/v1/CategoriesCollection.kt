package com.smtm.api.v1

import com.fasterxml.jackson.annotation.JsonProperty
import com.smtm.api.HalCollection
import com.smtm.api.Link

data class CategoriesCollection(
    @JsonProperty("_links") override val links: Map<String, Link>,
    @JsonProperty("count") override val count: Int,
    @JsonProperty("total") override val total: Int,
    @JsonProperty("_embedded") override val embedded: Map<String, Iterable<CategoryResource>>,
) : HalCollection(links, count, total, embedded)
