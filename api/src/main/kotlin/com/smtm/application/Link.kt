package com.smtm.application

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class Link @JsonCreator constructor(@JsonProperty("href") val href: String)
