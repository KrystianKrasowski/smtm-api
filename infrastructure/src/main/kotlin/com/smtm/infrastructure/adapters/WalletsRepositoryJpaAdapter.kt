package com.smtm.infrastructure.adapters

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.smtm.core.domain.OwnerId
import com.smtm.core.domain.tags.Tags
import com.smtm.core.domain.tags.TagsProblem
import com.smtm.core.domain.wallets.Wallet
import com.smtm.core.spi.WalletsRepository
import com.smtm.infrastructure.persistence.wallets.WalletSetEntity
import com.smtm.infrastructure.persistence.wallets.WalletSetsJpaRepository
import jakarta.persistence.EntityManager
import org.slf4j.LoggerFactory
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory
import org.springframework.transaction.annotation.Transactional

open class WalletsRepositoryJpaAdapter(
    entityManager: EntityManager,
    private val ownerIdProvider: () -> OwnerId,
) : WalletsRepository {

    private val repository = JpaRepositoryFactory(entityManager).getRepository(WalletSetsJpaRepository::class.java)
    private val logger = LoggerFactory.getLogger(WalletsRepositoryJpaAdapter::class.java)

    override fun getWallets(): Either<TagsProblem, Tags<Wallet>> =
        repository
            .runCatching { findByOwnerId(ownerIdProvider().asString()) }
            .map { it?.toDomain() ?: Tags.empty(ownerIdProvider().asEntityId()) }
            .map { it.right() }
            .onFailure { logger.error("Failed to fetch all categories", it) }
            .getOrElse { TagsProblem.from(it).left() }

    @Transactional
    override fun save(wallets: Tags<Wallet>): Either<TagsProblem, Tags<Wallet>> =
        repository
            .runCatching { save(WalletSetEntity.from(wallets)) }
            .map { it.toDomain() }
            .map { it.right() }
            .onFailure { logger.error("Failed to save categories", it) }
            .getOrElse { TagsProblem.from(it).left() }
}
