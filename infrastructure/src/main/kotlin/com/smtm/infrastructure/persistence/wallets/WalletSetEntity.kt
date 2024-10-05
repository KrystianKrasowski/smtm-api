package com.smtm.infrastructure.persistence.wallets

import com.smtm.core.domain.EntityId
import com.smtm.core.domain.tags.Tags
import com.smtm.core.domain.wallets.Wallet
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import jakarta.persistence.Version
import com.smtm.core.domain.Version.Companion as DomainEntityVersion

@Entity
@Table(name = "wallet_sets")
internal open class WalletSetEntity(

    @Id
    @Column(name = "owner_id")
    open val ownerId: String,

    @Version
    @Column(name = "version")
    open val version: Int,

    @OneToMany(mappedBy = "walletSet", cascade = [CascadeType.ALL], orphanRemoval = true)
    open val wallets: MutableList<WalletEntity>
) {

    fun toDomain(): Tags<Wallet> =
        Tags(
            id = EntityId.of(ownerId),
            version = DomainEntityVersion.of(version),
            tagsCollection = wallets.map { it.toDomain() }
        )

    companion object {

        fun from(categories: Tags<Wallet>): WalletSetEntity {
            val categorySetEntity = WalletSetEntity(
                ownerId = categories.id.asString(),
                version = categories.version.asInt(),
                wallets = mutableListOf()
            )
            val categoryEntities = categories.map { WalletEntity.from(it, categorySetEntity) }
            categorySetEntity.wallets.addAll(categoryEntities)
            return categorySetEntity
        }
    }
}
