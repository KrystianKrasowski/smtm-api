package com.smtm.core.service

import arrow.core.Either
import com.smtm.core.api.WalletsApi
import com.smtm.core.domain.EntityId
import com.smtm.core.domain.categories.Category
import com.smtm.core.domain.wallets.Wallets
import com.smtm.core.domain.wallets.WalletsProblem
import com.smtm.core.spi.WalletsRepository

internal class WalletsService(
    private val walletsRepository: WalletsRepository,
) : WalletsApi {

    override fun getAll(): Either<WalletsProblem, Wallets> =
        walletsRepository.getWallets()

    override fun create(category: Category): Either<WalletsProblem, Wallets> {
        TODO("Not yet implemented")
    }

    override fun update(category: Category): Either<WalletsProblem, Wallets> {
        TODO("Not yet implemented")
    }

    override fun delete(id: EntityId): Either<WalletsProblem, Wallets> {
        TODO("Not yet implemented")
    }
}
