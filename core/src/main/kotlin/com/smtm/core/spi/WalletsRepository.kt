package com.smtm.core.spi

import arrow.core.Either
import com.smtm.core.domain.tags.Tags
import com.smtm.core.domain.tags.TagsProblem
import com.smtm.core.domain.wallets.Wallet

interface WalletsRepository {

    fun getWallets(): Either<TagsProblem, Tags<Wallet>>

    fun save(wallets: Tags<Wallet>): Either<TagsProblem, Tags<Wallet>>
}
