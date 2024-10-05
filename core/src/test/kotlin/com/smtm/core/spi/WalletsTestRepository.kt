package com.smtm.core.spi

import arrow.core.Either
import arrow.core.right
import com.smtm.core.domain.EntityId
import com.smtm.core.domain.Version
import com.smtm.core.domain.tags.Tags
import com.smtm.core.domain.tags.TagsProblem
import com.smtm.core.domain.wallets.Wallet

class WalletsTestRepository : WalletsRepository {

    var walletsList: List<Wallet> = emptyList()

    private val wallets: Tags<Wallet>
        get() = Tags(
            id = EntityId.of("owner-jerry-smith"),
            version = Version.of(0),
            tagsCollection = walletsList
        )

    override fun getWallets(): Either<TagsProblem, Tags<Wallet>> {
        return wallets.right()
    }

    override fun save(wallets: Tags<Wallet>): Either<TagsProblem, Tags<Wallet>> {
        return wallets.right()
    }
}
