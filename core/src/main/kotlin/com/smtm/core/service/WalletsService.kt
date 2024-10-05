package com.smtm.core.service

import arrow.core.Either
import arrow.core.flatMap
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

    override fun create(wallet: Wallet): Either<WalletsProblem, Wallet> =
        walletsRepository
            .getWallets()
            .flatMap { it.add(wallet) }
            .flatMap { walletsRepository.save(it) }
            .flatMap { it.getById(wallet.id) }
            .mapLeft { WalletsProblem.from(it) }

    override fun update(wallet: Wallet): Either<WalletsProblem, Wallet> =
        walletsRepository
            .getWallets()
            .flatMap { it.replace(wallet) }
            .flatMap { walletsRepository.save(it) }
            .flatMap { it.getById(wallet.id) }
            .mapLeft { WalletsProblem.from(it) }

    override fun delete(id: EntityId): Either<WalletsProblem, EntityId> =
        walletsRepository
            .getWallets()
            .flatMap { it.delete(id) }
            .flatMap { walletsRepository.save(it) }
            .map { id }
            .mapLeft { WalletsProblem.from(it) }
}
