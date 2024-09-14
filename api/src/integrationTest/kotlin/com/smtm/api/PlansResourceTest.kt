package com.smtm.api

import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class PlansResourceTest {

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
        Environment.runSql("INSERT INTO category_sets (owner_id, version) VALUES (1, 1)")
        Environment.runSql("INSERT INTO categories (owner_id, name, icon) VALUES (1, 'Rent', 'HOUSE')")
        Environment.runSql("INSERT INTO categories (owner_id, name, icon) VALUES (1, 'Savings', 'PIGGY_BANK')")
        Environment.runSql("INSERT INTO categories (owner_id, name, icon) VALUES (1, 'Groceries', 'SHOPPING_CART')")

        Environment.runSql("""
            INSERT INTO plans
            (owner_id, version, name, start, "end") 
            VALUES 
            (1, 1, 'September 2024', '2024-09-01', '2024-09-30')
        """.trimIndent())

        Environment.runSql("""
            INSERT INTO plan_entries 
            (plan_id, category_id, amount, currency)
            VALUES
            (1, 1, 37959, 'PLN'),
            (1, 2, 500000, 'PLN'),
            (1, 3, 100000, 'PLN')
        """.trimIndent())
    }

    @AfterEach
    fun afterEach() {
        Environment.runSql("DELETE FROM plans CASCADE")
        Environment.runSql("DELETE FROM category_sets CASCADE")
        Environment.runSql("ALTER TABLE plans ALTER COLUMN id RESTART WITH 1")
        Environment.runSql("ALTER TABLE plan_entries ALTER COLUMN id RESTART WITH 1")
        Environment.runSql("ALTER TABLE categories ALTER COLUMN id RESTART WITH 1")
    }

    @Test
    @Disabled("Not implemented yet with this approach")
    fun `should return plan by id`() {
        Given {
            port(8080)
            header("Content-Type", "application/vnd.smtm.v1+json")
            header("Accept", "application/vnd.smtm.v1+json")
        } When {
            get("/plans/1")
        } Then {
            statusCode(200)
            header("Content-Type", "application/vnd.smtm.v1+json")
            body("_links.self.href", equalTo("http://localhost:8080/plans/1"))
            body("id", equalTo(1))
            body("name", equalTo("September 2024"))
            body("period.start", equalTo("2024-09-01"))
            body("period.end", equalTo("2024-09-30"))
            body("entries[0].category-id", equalTo(1))
            body("entries[0].value.amount", equalTo(37959))
            body("entries[0].value.currency", equalTo("PLN"))
            body("entries[1].category-id", equalTo(2))
            body("entries[1].value.amount", equalTo(500000))
            body("entries[1].value.currency", equalTo("PLN"))
            body("entries[2].category-id", equalTo(3))
            body("entries[2].value.amount", equalTo(100000))
            body("entries[2].value.currency", equalTo("PLN"))
            body("_embedded.categories[0]._links.self.href", equalTo("http://localhost:8080/categories/1"))
            body("_embedded.categories[0].id", equalTo(1))
            body("_embedded.categories[0].name", equalTo("Rent"))
            body("_embedded.categories[0].icon", equalTo("HOUSE"))
            body("_embedded.categories[1]._links.self.href", equalTo("http://localhost:8080/categories/2"))
            body("_embedded.categories[1].id", equalTo(2))
            body("_embedded.categories[1].name", equalTo("Savings"))
            body("_embedded.categories[1].icon", equalTo("PIGGY_BANK"))
            body("_embedded.categories[2]._links.self.href", equalTo("http://localhost:8080/categories/3"))
            body("_embedded.categories[2].id", equalTo(3))
            body("_embedded.categories[2].name", equalTo("Groceries"))
            body("_embedded.categories[2].icon", equalTo("SHOPPING_CART"))
        }
    }

    @Test
    @Disabled("Not implemented yet with this approach")
    fun `should return 404 while getting unknown plan`() {
        Given {
            port(8080)
            header("Content-Type", "application/vnd.smtm.v1+json")
            header("Accept", "application/vnd.smtm.v1+json")
        } When {
            get("/plans/99")
        } Then {
            statusCode(404)
            header("Content-Type", "application/problem+json")
            body("type", equalTo("https://api.smtm.com/problems/unknown-resource"))
            body("title", equalTo("Requested resource is unknown"))
        }
    }

    @Test
    @Disabled("Not implemented yet with this approach")
    fun `should create new plan`() {
        Given {
            port(8080)
            header("Content-Type", "application/vnd.smtm.v1+json")
            header("Accept", "application/vnd.smtm.v1+json")
        } When {
            post("/plans", """
                {
                    "name": "October 2024",
                    "period": {
                        "start": "2024-10-01",
                        "end": "2024-10-31",
                    },
                    "entries": [
                        {
                            "category-id": 1,
                            "value": {
                                "amount": 37959,
                                "currency": "PLN"
                            }
                        },
                        {
                            "category-id": 2,
                            "value": {
                                "amount": 600000,
                                "currency": "PLN"
                            }
                        },
                        {
                            "category-id": 3,
                            "value": {
                                "amount": 100000,
                                "currency": "PLN"
                            }
                        }
                    ]
                }
            """.trimIndent())
        } Then {
            statusCode(201)
            header("Location", "http://localhost:8080/plans/2")
        }
    }

    @Test
    @Disabled("Not implemented yet with this approach")
    fun `should return 422 while creating invalid plan`() {
        Given {
            port(8080)
            header("Content-Type", "application/vnd.smtm.v1+json")
            header("Accept", "application/vnd.smtm.v1+json")
        } When {
            body("""
                {
                    "name": "October <2024>",
                    "period": {
                        "start": "2024-10-01",
                        "end": "2024-09-30",
                    },
                    "entries": [
                        {
                            "category-id": 1,
                            "value": {
                                "amount": 37959,
                                "currency": "PLN"
                            }
                        },
                        {
                            "category-id": 2,
                            "value": {
                                "amount": 600000,
                                "currency": "PLN"
                            }
                        },
                        {
                            "category-id": 99,
                            "value": {
                                "amount": 100000,
                                "currency": "PLN"
                            }
                        }
                    ]
                }
            """.trimIndent())
            post("/plans")
        } Then {
            statusCode(422)
            header("Content-Type", "application/problem+json")
            body("type", equalTo("https://api.smtm.com/problems/constraint-violations"))
            body("title", equalTo("Provided resource is not valid"))
            body("violations[0].path", equalTo("/name"))
            body("violations[0].message", equalTo("contains illegal characters: %chars%"))
            body("violations[0].code", equalTo("ILLEGAL_CHARACTERS"))
            body("violations[0].parameters.chars", equalTo("<, >"))
            body("violations[1].path", equalTo("/period"))
            body("violations[1].message", equalTo("invalid date range"))
            body("violations[1].code", equalTo("INVALID_DATE_RANGE"))
            body("violations[2].path", equalTo("/entries/2/category-id"))
            body("violations[2].message", equalTo("unknown category"))
            body("violations[2].code", equalTo("UNKNOWN_CATEGORY"))
        }
    }

    @Test
    @Disabled("Not implemented yet with this approach")
    fun `should update plan by id`() {
        Given {
            port(8080)
            header("Content-Type", "application/vnd.smtm.v1+json")
            header("Accept", "application/vnd.smtm.v1+json")
        } When {
            body("""
                {
                    "id": 1,
                    "name": "Super September 2024",
                    "period": {
                        "start": "2024-09-01",
                        "end": "2024-09-30",
                    },
                    "entries": [
                        {
                            "category-id": 1,
                            "value": {
                                "amount": 41099,
                                "currency": "PLN"
                            }
                        },
                        {
                            "category-id": 2,
                            "value": {
                                "amount": 500000,
                                "currency": "PLN"
                            }
                        },
                        {
                            "category-id": 3,
                            "value": {
                                "amount": 100000,
                                "currency": "PLN"
                            }
                        }
                    ]
                }
            """.trimIndent())
            put("/plans/1")
        } Then {
            statusCode(204)
            header("Location", "http://localhost:8080/plans/1")
        }
    }

    @Test
    @Disabled("Not implemented yet with this approach")
    fun `should return 404 while updating unknown plan`() {
        Given {
            port(8080)
            header("Content-Type", "application/vnd.smtm.v1+json")
            header("Accept", "application/vnd.smtm.v1+json")
        } When {
            body("""
                {
                    "id": 1,
                    "name": "Super September 2024",
                    "period": {
                        "start": "2024-09-01",
                        "end": "2024-09-30",
                    },
                    "entries": [
                        {
                            "category-id": 1,
                            "value": {
                                "amount": 41099,
                                "currency": "PLN"
                            }
                        },
                        {
                            "category-id": 2,
                            "value": {
                                "amount": 500000,
                                "currency": "PLN"
                            }
                        },
                        {
                            "category-id": 3,
                            "value": {
                                "amount": 100000,
                                "currency": "PLN"
                            }
                        }
                    ]
                }
            """.trimIndent())
            put("/plans/99")
        } Then {
            statusCode(404)
            header("Content-Type", "application/problem+json")
            body("type", equalTo("https://api.smtm.com/problems/unknown-resource"))
            body("title", equalTo("Requested resource is unknown"))
        }
    }

    @Test
    @Disabled("Not implemented yet with this approach")
    fun `should return 422 while updating invalid plan`() {
        Given {
            port(8080)
            header("Content-Type", "application/vnd.smtm.v1+json")
            header("Accept", "application/vnd.smtm.v1+json")
        } When {
            body("""
                {
                    "name": "September <2024>",
                    "period": {
                        "start": "2024-10-01",
                        "end": "2024-09-30",
                    },
                    "entries": [
                        {
                            "category-id": 1,
                            "value": {
                                "amount": 37959,
                                "currency": "PLN"
                            }
                        },
                        {
                            "category-id": 2,
                            "value": {
                                "amount": 600000,
                                "currency": "PLN"
                            }
                        },
                        {
                            "category-id": 99,
                            "value": {
                                "amount": 100000,
                                "currency": "PLN"
                            }
                        }
                    ]
                }
            """.trimIndent())
            put("/plans/1")
        } Then {
            statusCode(422)
            header("Content-Type", "application/problem+json")
            body("type", equalTo("https://api.smtm.com/problems/constraint-violations"))
            body("title", equalTo("Provided resource is not valid"))
            body("violations[0].path", equalTo("/name"))
            body("violations[0].message", equalTo("contains illegal characters: %chars%"))
            body("violations[0].code", equalTo("ILLEGAL_CHARACTERS"))
            body("violations[0].parameters.chars", equalTo("<, >"))
            body("violations[1].path", equalTo("/period"))
            body("violations[1].message", equalTo("invalid date range"))
            body("violations[1].code", equalTo("INVALID_DATE_RANGE"))
            body("violations[2].path", equalTo("/entries/2/category-id"))
            body("violations[2].message", equalTo("unknown category"))
            body("violations[2].code", equalTo("UNKNOWN_CATEGORY"))
        }
    }
}
