package com.smtm.application.v1

import com.fasterxml.jackson.annotation.JsonProperty
import com.smtm.application.HalResource
import com.smtm.application.Link

interface RootApi {

    fun getRoot(): RootDto
}

data class RootDto(
    @JsonProperty("_links") override val links: Map<String, Link>
) : HalResource(links)