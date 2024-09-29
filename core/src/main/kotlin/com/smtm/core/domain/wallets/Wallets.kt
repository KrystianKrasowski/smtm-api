package com.smtm.core.domain.wallets

import com.smtm.core.domain.OwnerId
import com.smtm.core.domain.Version

data class Wallets(
    val id: OwnerId,
    val version: Version,
    private val actual: List<Wallet>
) : Iterable<Wallet> {

    val size: Int =
        actual.size

    override fun iterator(): Iterator<Wallet> {
        return actual.iterator()
    }

    companion object {

        fun empty(ownerId: OwnerId): Wallets =
            Wallets(ownerId, Version.of(0), emptyList())
    }
}
