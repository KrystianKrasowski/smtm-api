package com.smtm.core.service

import arrow.core.Either
import com.smtm.core.api.WalletsApi
import com.smtm.core.domain.EntityId
import com.smtm.core.domain.wallets.Wallet
import com.smtm.core.domain.wallets.WalletsProblem
import com.smtm.core.spi.WalletsRepository

internal class WalletsService(
    private val walletsRepository: WalletsRepository,
) : WalletsApi {

    override fun getAll(): Either<WalletsProblem, Collection<Wallet>> =
        walletsRepository
            .getWallets()
            .map { it.toList() }
            .mapLeft { WalletsProblem.from(it) }

    override fun create(category: Wallet): Either<WalletsProblem, Wallet> {
        TODO("Not yet implemented")
    }

    override fun update(category: Wallet): Either<WalletsProblem, Wallet> {
        TODO("Not yet implemented")
    }

    override fun delete(id: EntityId): Either<WalletsProblem, EntityId> {
        TODO("Not yet implemented")
    }
}
