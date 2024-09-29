package com.smtm.api.v1

import com.fasterxml.jackson.annotation.JsonProperty
import com.smtm.api.HalResource
import com.smtm.api.Link

data class WalletResource(
    @JsonProperty("_links") override val links: Map<String, Link>,
    @JsonProperty("id") override val id: String,
    private val wallet: WalletDto
) : HalResource<String>(links, id) {

    @JsonProperty("name") val name: String = wallet.name
    @JsonProperty("icon") val icon: String = wallet.icon
}
