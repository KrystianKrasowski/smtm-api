package com.smtm.core.api

import arrow.core.Either
import com.smtm.core.domain.EntityId
import com.smtm.core.domain.categories.Category
import com.smtm.core.domain.wallets.Wallets
import com.smtm.core.domain.wallets.WalletsProblem
import com.smtm.core.service.WalletsService
import com.smtm.core.spi.WalletsRepository

interface WalletsApi {

    fun getAll(): Either<WalletsProblem, Wallets>

    fun create(category: Category): Either<WalletsProblem, Wallets>

    fun update(category: Category): Either<WalletsProblem, Wallets>

    fun delete(id: EntityId): Either<WalletsProblem, Wallets>

    companion object {

        fun create(walletsRepository: WalletsRepository): WalletsApi =
            WalletsService(walletsRepository)
    }
}
