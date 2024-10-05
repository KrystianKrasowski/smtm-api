package com.smtm.core.service

import assertk.assertThat
import assertk.assertions.containsAtLeast
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotNull
import com.smtm.core.World
import com.smtm.core.domain.EntityId
import com.smtm.core.domain.Violation
import com.smtm.core.domain.wallets.Wallet
import com.smtm.core.domain.wallets.WalletsProblem
import io.cucumber.java.en.Then
import io.cucumber.java.en.When

class WalletsServiceStepdefs(private val world: World) {

    private val categoriesService get() = WalletsService(world.walletsRepository)

    private var problem: WalletsProblem? = null

    @When("user creates new wallet")
    fun `user creates new wallet`(wallet: Wallet) {
        categoriesService.create(wallet)
            .onLeft { problem = it }
    }

    @When("user updates the wallet")
    fun `user updates the wallet`(wallet: Wallet) {
        categoriesService.update(wallet)
            .onLeft { problem = it }
    }

    @When("user deletes wallet of id {string}")
    fun `user deletes wallet of id N`(id: String) {
        categoriesService.delete(EntityId.of(id))
            .onLeft { problem = it }
    }

    @Then("wallet is not saved due to constraint violation")
    fun `wallet is not saved due to constraint violation`(violation: Violation) {
        assertThat(problem)
            .isNotNull()
            .isInstanceOf(WalletsProblem.ValidationErrors::class)
            .containsAtLeast(violation)
    }

    @Then("unknown wallet problem occurs")
    fun `unknown wallet problem occurs`() {
        assertThat(problem)
            .isNotNull()
            .isInstanceOf(WalletsProblem.Unknown::class)
    }
}
