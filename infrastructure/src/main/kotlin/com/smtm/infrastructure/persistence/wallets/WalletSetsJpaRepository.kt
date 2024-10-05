package com.smtm.infrastructure.persistence.wallets

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
internal interface WalletSetsJpaRepository : JpaRepository<WalletSetEntity, String> {

    fun findByOwnerId(ownerId: String): WalletSetEntity?
}
