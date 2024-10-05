package com.smtm.core.spi

import com.smtm.core.World
import com.smtm.core.domain.wallets.Wallet
import io.cucumber.java.en.Given

class WalletsRepositoryStepdefs(private val world: World) {

    @Given("user wallets are")
    fun `user categories are`(wallets: List<Wallet>) {
        world.walletsRepository.walletsList = wallets
    }
}
