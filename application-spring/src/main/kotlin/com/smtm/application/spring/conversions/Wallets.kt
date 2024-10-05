package com.smtm.application.spring.conversions

import com.smtm.api.LinkFactory
import com.smtm.api.ResourcePaths
import com.smtm.api.v1.WalletDto
import com.smtm.api.v1.WalletResource
import com.smtm.api.v1.WalletsCollection
import com.smtm.core.domain.EntityId
import com.smtm.core.domain.Icon
import com.smtm.core.domain.wallets.Wallet

internal object Wallets {

    fun Collection<Wallet>.toHalCollection(linkFactory: LinkFactory): WalletsCollection =
        WalletsCollection(
            links = mapOf(
                "self" to linkFactory.create(ResourcePaths.WALLETS)
            ),
            count = size,
            total = size,
            embedded = mapOf(
                "wallets" to map { it.toResource(linkFactory) }
            )
        )

    fun Wallet.toResource(linkFactory: LinkFactory): WalletResource =
        WalletResource(
            links = mapOf(
                "self" to linkFactory.create("${ResourcePaths.WALLETS}/$id")
            ),
            id = id.asString(),
            wallet = WalletDto(
                name = name,
                icon = icon.name
            )
        )

    fun WalletDto.toDomain(id: String? = null): Wallet =
        id?.let { EntityId.of(it) }
            ?.let { Wallet.of(it, name, Icon.valueOfOrDefault(icon)) }
            ?: Wallet.newOf(name, Icon.valueOfOrDefault(icon))
}
