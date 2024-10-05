package com.smtm.api

import com.smtm.api.matchers.matchesNamedPattern
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

    @Test
    fun `should create new wallet`() {
        Given {
            port(8080)
            header("Content-Type", "application/vnd.smtm.v1+json")
            header("Accept", "application/vnd.smtm.v1+json")
            header("Authorization", "Bearer ${Environment.getAccessToken("owner-1")}")
            body("""{"name": "Another bank account", "icon": "WALLET"}""")
        } When {
            post("/wallets")
        } Then {
            statusCode(201)
            header("Content-Type", "application/vnd.smtm.v1+json")
            header("Location", matchesNamedPattern("^http://localhost:8080/wallets/wallet-%uuid%$"))
            body("_links.self.href", matchesNamedPattern("^http://localhost:8080/wallets/wallet-%uuid%$"))
            body("name", equalTo("Another bank account"))
            body("icon", equalTo("WALLET"))
        }
    }

    @Test
    fun `should return non unique constraint violation`() {
        Given {
            port(8080)
            header("Content-Type", "application/vnd.smtm.v1+json")
            header("Accept", "application/vnd.smtm.v1+json")
            header("Authorization", "Bearer ${Environment.getAccessToken("owner-1")}")
            body("""{"name": "Bank account", "icon": "WALLET"}""")
        } When {
            post("/wallets")
        } Then {
            statusCode(422)
            header("Content-Type", "application/problem+json")
            body("type", equalTo("https://api.smtm.com/problems/constraint-violations"))
            body("title", equalTo("Provided resource is not valid"))
            body("violations[0].path", equalTo("name"))
            body("violations[0].message", equalTo("NON_UNIQUE"))
            body("violations[0].code", equalTo("NON_UNIQUE"))
        }
    }

    @Test
    fun `should return invalid characters constraint violation for POST request`() {
        Given {
            port(8080)
            header("Content-Type", "application/vnd.smtm.v1+json")
            header("Accept", "application/vnd.smtm.v1+json")
            header("Authorization", "Bearer ${Environment.getAccessToken("owner-1")}")
            body("""{"name": "Invalid <name>", "icon": "BANK"}""")
        } When {
            post("/wallets")
        } Then {
            statusCode(422)
            header("Content-Type", "application/problem+json")
            body("type", equalTo("https://api.smtm.com/problems/constraint-violations"))
            body("title", equalTo("Provided resource is not valid"))
            body("violations[0].path", equalTo("name"))
            body("violations[0].message", equalTo("ILLEGAL_CHARACTERS"))
            body("violations[0].code", equalTo("ILLEGAL_CHARACTERS"))
            body("violations[0].parameters['illegal-characters']", equalTo("<, >"))
        }
    }

    @Test
    fun `should update existing wallet`() {
        Given {
            port(8080)
            header("Content-Type", "application/vnd.smtm.v1+json")
            header("Accept", "application/vnd.smtm.v1+json")
            header("Authorization", "Bearer ${Environment.getAccessToken("owner-1")}")
            body("""{"name": "Awesome bank account", "icon": "BANK"}""")
        } When {
            put("/wallets/wallet-1")
        } Then {
            statusCode(200)
            header("Content-Type", "application/vnd.smtm.v1+json")
            body("_links.self.href", equalTo("http://localhost:8080/wallets/wallet-1"))
            body("name", equalTo("Awesome bank account"))
            body("icon", equalTo("BANK"))
        }
    }

    @Test
    fun `should return 404 while updating non existing wallet`() {
        Given {
            port(8080)
            header("Content-Type", "application/vnd.smtm.v1+json")
            header("Accept", "application/vnd.smtm.v1+json")
            header("Authorization", "Bearer ${Environment.getAccessToken("owner-1")}")
            body("""{"name": "Awesome bank account", "icon": "BANK"}""")
        } When {
            put("/wallets/999")
        } Then {
            statusCode(404)
            header("Content-Type", "application/problem+json")
            body("type", equalTo("https://api.smtm.com/problems/unknown-resource"))
            body("title", equalTo("Requested resource is unknown"))
        }
    }

    @Test
    fun `should return invalid characters constraint violation for PUT request`() {
        Given {
            port(8080)
            header("Content-Type", "application/vnd.smtm.v1+json")
            header("Accept", "application/vnd.smtm.v1+json")
            header("Authorization", "Bearer ${Environment.getAccessToken("owner-1")}")
            body("""{"name": "Invalid <name>", "icon": "BANK"}""")
        } When {
            put("/wallets/wallet-1")
        } Then {
            statusCode(422)
            header("Content-Type", "application/problem+json")
            body("type", equalTo("https://api.smtm.com/problems/constraint-violations"))
            body("title", equalTo("Provided resource is not valid"))
            body("violations[0].path", equalTo("name"))
            body("violations[0].message", equalTo("ILLEGAL_CHARACTERS"))
            body("violations[0].code", equalTo("ILLEGAL_CHARACTERS"))
            body("violations[0].parameters['illegal-characters']", equalTo("<, >"))
        }
    }

    @Test
    fun `should delete existing wallet`() {
        Given {
            port(8080)
            header("Authorization", "Bearer ${Environment.getAccessToken("owner-1")}")
        } When {
            delete("/wallets/wallet-1")
        } Then {
            statusCode(204)
        }
    }

    @Test
    fun `should return 404 while deleting non existing wallet`() {
        Given {
            port(8080)
            header("Authorization", "Bearer ${Environment.getAccessToken("owner-1")}")
        } When {
            delete("/wallets/999")
        } Then {
            statusCode(404)
            header("Content-Type", "application/problem+json")
            body("type", equalTo("https://api.smtm.com/problems/unknown-resource"))
            body("title", equalTo("Requested resource is unknown"))
        }
    }
}
