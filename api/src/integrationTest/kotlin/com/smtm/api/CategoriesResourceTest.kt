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
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class CategoriesResourceTest {

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
        Environment.runSql("INSERT INTO category_sets (owner_id, version) VALUES ('owner-1', 1)")
        Environment.runSql("INSERT INTO categories (id, owner_id, name, icon) VALUES ('1', 'owner-1', 'Rent', 'HOUSE')")
        Environment.runSql("INSERT INTO categories (id, owner_id, name, icon) VALUES ('2', 'owner-1', 'Savings', 'PIGGY_BANK')")
        Environment.runSql("INSERT INTO categories (id, owner_id, name, icon) VALUES ('3', 'owner-1', 'Groceries', 'SHOPPING_CART')")
    }

    @AfterEach
    fun afterEach() {
        Environment.runSql("DELETE FROM category_sets CASCADE")
    }

    @Test
    fun `should get all categories`() {
        Given {
            port(8080)
            header("Accept", "application/vnd.smtm.v1+json")
            header("Authorization", "Bearer ${Environment.getAccessToken("owner-1")}")
        } When {
            get("/categories")
        } Then {
            statusCode(200)
            header("Content-Type", "application/vnd.smtm.v1+json")
            body("_links.self.href", equalTo("http://localhost:8080/categories"))
            body("count", equalTo(3))
            body("total", equalTo(3))
            body("_embedded.categories", hasSize<Any>(3))
            body("_embedded.categories[0]._links.self.href", equalTo("http://localhost:8080/categories/1"))
            body("_embedded.categories[0].id", equalTo("1"))
            body("_embedded.categories[0].name", equalTo("Rent"))
            body("_embedded.categories[0].icon", equalTo("HOUSE"))
            body("_embedded.categories[1]._links.self.href", equalTo("http://localhost:8080/categories/2"))
            body("_embedded.categories[1].id", equalTo("2"))
            body("_embedded.categories[1].name", equalTo("Savings"))
            body("_embedded.categories[1].icon", equalTo("PIGGY_BANK"))
            body("_embedded.categories[2]._links.self.href", equalTo("http://localhost:8080/categories/3"))
            body("_embedded.categories[2].id", equalTo("3"))
            body("_embedded.categories[2].name", equalTo("Groceries"))
            body("_embedded.categories[2].icon", equalTo("SHOPPING_CART"))
        }
    }

    @Test
    fun `should create new category`() {
        Given {
            port(8080)
            header("Content-Type", "application/vnd.smtm.v1+json")
            header("Accept", "application/vnd.smtm.v1+json")
            header("Authorization", "Bearer ${Environment.getAccessToken("owner-1")}")
        } When {
            body("""{"name": "Services", "icon": "LIGHTENING"}""")
            post("/categories")
        } Then {
            statusCode(201)
            header("Content-Type", "application/vnd.smtm.v1+json")
            header("Location", matchesNamedPattern("^http://localhost:8080/categories/category-%uuid%$"))
            body("_links.self.href", matchesNamedPattern("^http://localhost:8080/categories/category-%uuid%$"))
            body("name", equalTo("Services"))
            body("icon", equalTo("LIGHTENING"))
        }
    }

    @Test
    fun `should return non unique constraint violation`() {
        Given {
            port(8080)
            header("Content-Type", "application/vnd.smtm.v1+json")
            header("Accept", "application/vnd.smtm.v1+json")
            header("Authorization", "Bearer ${Environment.getAccessToken("owner-1")}")
        } When {
            body("""{"name": "Rent", "icon": "HOUSE"}""")
            post("/categories")
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
        } When {
            body("""{"name": "Invalid <name>", "icon": "HOUSE"}""")
            post("/categories")
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
    @Disabled
    fun `should update existing category`() {
        Given {
            port(8080)
            header("Content-Type", "application/vnd.smtm.v1+json")
            header("Accept", "application/vnd.smtm.v1+json")
        } When {
            body("""{"name": "My rent", "icon": "FOLDER"}""")
            put("/categories/1")
        } Then {
            statusCode(200)
            header("Content-Type", "application/vnd.smtm.v1+json")
            body("_links.self.href", equalTo("http://localhost:8080/categories/1"))
            body("id", equalTo(1))
            body("name", equalTo("My rent"))
            body("icon", equalTo("FOLDER"))
        }
    }

    @Test
    @Disabled
    fun `should return invalid characters constraint violation for PUT request`() {
        Given {
            port(8080)
            header("Content-Type", "application/vnd.smtm.v1+json")
            header("Accept", "application/vnd.smtm.v1+json")
        } When {
            body("""{"name": "Invalid <name>", "icon": "HOUSE"}""")
            put("/categories/1")
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
}
