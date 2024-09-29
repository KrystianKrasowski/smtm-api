package com.smtm.infrastructure.persistence.wallets

import com.smtm.core.domain.EntityId
import com.smtm.core.domain.Icon
import com.smtm.core.domain.wallets.Wallet
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "wallets")
internal open class WalletEntity(

    @Id
    @Column(name = "id")
    open val id: String,

    @Column(name = "name", unique = true)
    open val name: String,

    @Column(name = "icon")
    open val icon: String,

    @ManyToOne
    @JoinColumn(name = "owner_id")
    open val walletSet: WalletSetEntity
) {

    fun toDomain(): Wallet =
        Wallet(
            id = EntityId.of(id),
            name = name,
            icon = Icon.valueOfOrDefault(icon)
        )

    companion object {

        fun from(wallet: Wallet, set: WalletSetEntity): WalletEntity =
            WalletEntity(
                id = wallet.id.asString(),
                name = wallet.name,
                icon = wallet.icon.name,
                walletSet = set
            )
    }
}
