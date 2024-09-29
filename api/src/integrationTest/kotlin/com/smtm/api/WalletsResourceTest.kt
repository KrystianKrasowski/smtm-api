package com.smtm.api

import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class WalletsResourceTest {

    companion object {

        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            Environment.setUp()
        }

        @AfterAll
        @JvmStatic
        fun afterAll() {
            Environment.tearDown()
        }
    }

    @BeforeEach
    fun beforeEach() {
        Environment.runSql("INSERT INTO wallet_sets (owner_id, version) VALUES ('owner-1', 1)")
        Environment.runSql("INSERT INTO wallets (id, owner_id, name, icon) VALUES ('wallet-1', 'owner-1', 'Bank account', 'WALLET')")
        Environment.runSql("INSERT INTO wallets (id, owner_id, name, icon) VALUES ('wallet-2', 'owner-1', 'Cash', 'WALLET')")
    }

    @AfterEach
    fun afterEach() {
        Environment.runSql("DELETE FROM wallet_sets CASCADE")
    }

    @Test
    fun `should get all wallets`() {
        Given {
            port(8080)
            header("Accept", "application/vnd.smtm.v1+json")
            header("Authorization", "Bearer ${Environment.getAccessToken("owner-1")}")
        } When {
            get("/wallets")
        } Then {
            statusCode(200)
            header("Content-Type", "application/vnd.smtm.v1+json")
            body("_links.self.href", equalTo("http://localhost:8080/wallets"))
            body("count", equalTo(2))
            body("total", equalTo(2))
            body("_embedded.wallets", hasSize<Any>(2))
            body("_embedded.wallets[0]._links.self.href", equalTo("http://localhost:8080/wallets/wallet-1"))
            body("_embedded.wallets[0].id", equalTo("wallet-1"))
            body("_embedded.wallets[0].name", equalTo("Bank account"))
            body("_embedded.wallets[0].icon", equalTo("WALLET"))
            body("_embedded.wallets[1]._links.self.href", equalTo("http://localhost:8080/wallets/wallet-2"))
            body("_embedded.wallets[1].id", equalTo("wallet-2"))
            body("_embedded.wallets[1].name", equalTo("Cash"))
            body("_embedded.wallets[1].icon", equalTo("WALLET"))
        }
    }
}
