package com.smtm.api.v1

import com.fasterxml.jackson.annotation.JsonProperty
import com.smtm.api.HalResource
import com.smtm.api.Link

data class RootDto(
    @JsonProperty("_links") override val links: Map<String, Link>
) : HalResource<Long>(links, 1)
