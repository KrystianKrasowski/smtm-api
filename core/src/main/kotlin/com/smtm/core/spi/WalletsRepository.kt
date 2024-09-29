package com.smtm.core.spi

import arrow.core.Either
import com.smtm.core.domain.wallets.Wallets
import com.smtm.core.domain.wallets.WalletsProblem

interface WalletsRepository {

    fun getWallets(): Either<WalletsProblem, Wallets>

    fun save(wallets: Wallets): Either<WalletsProblem, Wallets>
}
