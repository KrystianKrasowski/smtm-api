package com.smtm.core.api

import arrow.core.Either
import com.smtm.core.domain.EntityId
import com.smtm.core.domain.wallets.Wallet
import com.smtm.core.domain.wallets.WalletsProblem
import com.smtm.core.service.WalletsService
import com.smtm.core.spi.WalletsRepository

interface WalletsApi {

    fun getAll(): Either<WalletsProblem, Collection<Wallet>>

    fun create(wallet: Wallet): Either<WalletsProblem, Wallet>

    fun update(wallet: Wallet): Either<WalletsProblem, Wallet>

    fun delete(id: EntityId): Either<WalletsProblem, EntityId>

    companion object {

        fun create(walletsRepository: WalletsRepository): WalletsApi =
            WalletsService(walletsRepository)
    }
}
