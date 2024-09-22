package com.smtm.api.v1

import com.fasterxml.jackson.annotation.JsonProperty

data class CategoryDto(
    @JsonProperty("name") val name: String,
    @JsonProperty("icon") val icon: String
)
