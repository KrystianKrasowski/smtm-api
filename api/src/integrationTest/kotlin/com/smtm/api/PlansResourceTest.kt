package com.smtm.api

import com.smtm.api.matchers.matchesNamedPattern
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
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
        Environment.runSql("INSERT INTO category_sets (owner_id, version) VALUES ('owner-1', 1)")
        Environment.runSql("INSERT INTO categories (id, owner_id, name, icon) VALUES ('smtm-cat-1', 'owner-1', 'Rent', 'HOUSE')")
        Environment.runSql("INSERT INTO categories (id, owner_id, name, icon) VALUES ('smtm-cat-2', 'owner-1', 'Savings', 'PIGGY_BANK')")
        Environment.runSql("INSERT INTO categories (id, owner_id, name, icon) VALUES ('smtm-cat-3', 'owner-1', 'Groceries', 'SHOPPING_CART')")

        Environment.runSql(
            """
            INSERT INTO plans
            (id, owner_id, version, name, start, "end") 
            VALUES 
            ('plan-1', 'owner-1', 1, 'September 2024', '2024-09-01', '2024-09-30')
        """.trimIndent()
        )

        Environment.runSql(
            """
            INSERT INTO plan_entries 
            (plan_id, category_id, amount, currency)
            VALUES
            ('plan-1', 'smtm-cat-1', 37959, 'PLN'),
            ('plan-1', 'smtm-cat-2', 500000, 'PLN'),
            ('plan-1', 'smtm-cat-3', 100000, 'PLN')
        """.trimIndent()
        )
    }

    @AfterEach
    fun afterEach() {
        Environment.runSql("DELETE FROM plans CASCADE")
        Environment.runSql("DELETE FROM category_sets CASCADE")
    }

    @Test
    fun `should return plan by id`() {
        Given {
            port(8080)
            header("Content-Type", "application/vnd.smtm.v1+json")
            header("Accept", "application/vnd.smtm.v1+json")
            header("Authorization", "Bearer ${Environment.getAccessToken("owner-1")}")
        } When {
            get("/plans/plan-1")
        } Then {
            statusCode(200)
            header("Content-Type", "application/vnd.smtm.v1+json")
            body("_links.self.href", equalTo("http://localhost:8080/plans/plan-1"))
            body("name", equalTo("September 2024"))
            body("period.start", equalTo("2024-09-01"))
            body("period.end", equalTo("2024-09-30"))
            body("entries[0].category-id", equalTo("smtm-cat-1"))
            body("entries[0].value.amount", equalTo(37959))
            body("entries[0].value.currency", equalTo("PLN"))
            body("entries[1].category-id", equalTo("smtm-cat-2"))
            body("entries[1].value.amount", equalTo(500000))
            body("entries[1].value.currency", equalTo("PLN"))
            body("entries[2].category-id", equalTo("smtm-cat-3"))
            body("entries[2].value.amount", equalTo(100000))
            body("entries[2].value.currency", equalTo("PLN"))
            body("_embedded.categories[0]._links.self.href", equalTo("http://localhost:8080/categories/smtm-cat-1"))
            body("_embedded.categories[0].id", equalTo("smtm-cat-1"))
            body("_embedded.categories[0].name", equalTo("Rent"))
            body("_embedded.categories[0].icon", equalTo("HOUSE"))
            body("_embedded.categories[1]._links.self.href", equalTo("http://localhost:8080/categories/smtm-cat-2"))
            body("_embedded.categories[1].id", equalTo("smtm-cat-2"))
            body("_embedded.categories[1].name", equalTo("Savings"))
            body("_embedded.categories[1].icon", equalTo("PIGGY_BANK"))
            body("_embedded.categories[2]._links.self.href", equalTo("http://localhost:8080/categories/smtm-cat-3"))
            body("_embedded.categories[2].id", equalTo("smtm-cat-3"))
            body("_embedded.categories[2].name", equalTo("Groceries"))
            body("_embedded.categories[2].icon", equalTo("SHOPPING_CART"))
        }
    }

    @Test
    fun `should return 404 while getting unknown plan`() {
        Given {
            port(8080)
            header("Content-Type", "application/vnd.smtm.v1+json")
            header("Accept", "application/vnd.smtm.v1+json")
            header("Authorization", "Bearer ${Environment.getAccessToken("owner-1")}")
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
    fun `should create new plan`() {
        Given {
            port(8080)
            header("Content-Type", "application/vnd.smtm.v1+json")
            header("Accept", "application/vnd.smtm.v1+json")
            header("Authorization", "Bearer ${Environment.getAccessToken("owner-1")}")
            body(
                """
                {
                    "name": "October 2024",
                    "period": {
                        "start": "2024-10-01",
                        "end": "2024-10-31"
                    },
                    "entries": [
                        {
                            "category-id": "smtm-cat-1",
                            "value": {
                                "amount": 37959,
                                "currency": "PLN"
                            }
                        },
                        {
                            "category-id": "smtm-cat-2",
                            "value": {
                                "amount": 600000,
                                "currency": "PLN"
                            }
                        },
                        {
                            "category-id": "smtm-cat-3",
                            "value": {
                                "amount": 100000,
                                "currency": "PLN"
                            }
                        }
                    ]
                }
            """.trimIndent()
            )
        } When {
            post("/plans")
        } Then {
            statusCode(201)
            header("Location", matchesNamedPattern("^http://localhost:8080/plans/plan-%uuid%$"))
            header("Content-Type", "application/vnd.smtm.v1+json")
            body("_links.self.href", matchesNamedPattern("^http://localhost:8080/plans/plan-%uuid%$"))
            body("name", equalTo("October 2024"))
            body("period.start", equalTo("2024-10-01"))
            body("period.end", equalTo("2024-10-31"))
            body("entries[0].category-id", equalTo("smtm-cat-1"))
            body("entries[0].value.amount", equalTo(37959))
            body("entries[0].value.currency", equalTo("PLN"))
            body("entries[1].category-id", equalTo("smtm-cat-2"))
            body("entries[1].value.amount", equalTo(600000))
            body("entries[1].value.currency", equalTo("PLN"))
            body("entries[2].category-id", equalTo("smtm-cat-3"))
            body("entries[2].value.amount", equalTo(100000))
            body("entries[2].value.currency", equalTo("PLN"))
            body("_embedded.categories[0]._links.self.href", equalTo("http://localhost:8080/categories/smtm-cat-1"))
            body("_embedded.categories[0].id", equalTo("smtm-cat-1"))
            body("_embedded.categories[0].name", equalTo("Rent"))
            body("_embedded.categories[0].icon", equalTo("HOUSE"))
            body("_embedded.categories[1]._links.self.href", equalTo("http://localhost:8080/categories/smtm-cat-2"))
            body("_embedded.categories[1].id", equalTo("smtm-cat-2"))
            body("_embedded.categories[1].name", equalTo("Savings"))
            body("_embedded.categories[1].icon", equalTo("PIGGY_BANK"))
            body("_embedded.categories[2]._links.self.href", equalTo("http://localhost:8080/categories/smtm-cat-3"))
            body("_embedded.categories[2].id", equalTo("smtm-cat-3"))
            body("_embedded.categories[2].name", equalTo("Groceries"))
            body("_embedded.categories[2].icon", equalTo("SHOPPING_CART"))
        }
    }

    @Test
    fun `should return 422 while creating invalid plan`() {
        Given {
            port(8080)
            header("Content-Type", "application/vnd.smtm.v1+json")
            header("Accept", "application/vnd.smtm.v1+json")
            header("Authorization", "Bearer ${Environment.getAccessToken("owner-1")}")
            body(
                """
                {
                    "name": "October <2024>",
                    "period": {
                        "start": "2024-10-01",
                        "end": "2024-09-30"
                    },
                    "entries": [
                        {
                            "category-id": "smtm-cat-1",
                            "value": {
                                "amount": 37959,
                                "currency": "PLN"
                            }
                        },
                        {
                            "category-id": "smtm-cat-2",
                            "value": {
                                "amount": 600000,
                                "currency": "PLN"
                            }
                        },
                        {
                            "category-id": "smtm-cat-99",
                            "value": {
                                "amount": 100000,
                                "currency": "PLN"
                            }
                        }
                    ]
                }
            """.trimIndent()
            )
        } When {
            post("/plans")
        } Then {
            statusCode(422)
            header("Content-Type", "application/problem+json")
            body("type", equalTo("https://api.smtm.com/problems/constraint-violations"))
            body("title", equalTo("Provided resource is not valid"))
            body("violations[0].path", equalTo("name"))
            body("violations[0].code", equalTo("ILLEGAL_CHARACTERS"))
            body("violations[0].parameters.illegal-characters", equalTo("<, >"))
            body("violations[1].path", equalTo("period"))
            body("violations[1].code", equalTo("INVALID"))
            body("violations[2].path", equalTo("entries/3/category"))
            body("violations[2].code", equalTo("UNKNOWN"))
        }
    }

    @Test
    fun `should update plan by id`() {
        Given {
            port(8080)
            header("Content-Type", "application/vnd.smtm.v1+json")
            header("Accept", "application/vnd.smtm.v1+json")
            header("Authorization", "Bearer ${Environment.getAccessToken("owner-1")}")
            body(
                """
                {
                    "id": 1,
                    "name": "Super September 2024",
                    "period": {
                        "start": "2024-09-01",
                        "end": "2024-09-30"
                    },
                    "entries": [
                        {
                            "category-id": "smtm-cat-1",
                            "value": {
                                "amount": 41099,
                                "currency": "PLN"
                            }
                        },
                        {
                            "category-id": "smtm-cat-2",
                            "value": {
                                "amount": 500000,
                                "currency": "PLN"
                            }
                        },
                        {
                            "category-id": "smtm-cat-3",
                            "value": {
                                "amount": 100000,
                                "currency": "PLN"
                            }
                        }
                    ]
                }
            """.trimIndent()
            )
        } When {
            put("/plans/plan-1")
        } Then {
            statusCode(204)
        }
    }

    @Test
    fun `should return 404 while updating unknown plan`() {
        Given {
            port(8080)
            header("Content-Type", "application/vnd.smtm.v1+json")
            header("Accept", "application/vnd.smtm.v1+json")
            header("Authorization", "Bearer ${Environment.getAccessToken("owner-1")}")
            body(
                """
                {
                    "id": 1,
                    "name": "Super September 2024",
                    "period": {
                        "start": "2024-09-01",
                        "end": "2024-09-30"
                    },
                    "entries": [
                        {
                            "category-id": "smtm-cat-1",
                            "value": {
                                "amount": 41099,
                                "currency": "PLN"
                            }
                        },
                        {
                            "category-id": "smtm-cat-2",
                            "value": {
                                "amount": 500000,
                                "currency": "PLN"
                            }
                        },
                        {
                            "category-id": "smtm-cat-3",
                            "value": {
                                "amount": 100000,
                                "currency": "PLN"
                            }
                        }
                    ]
                }
            """.trimIndent()
            )
        } When {
            put("/plans/99")
        } Then {
            statusCode(404)
            header("Content-Type", "application/problem+json")
            body("type", equalTo("https://api.smtm.com/problems/unknown-resource"))
            body("title", equalTo("Requested resource is unknown"))
        }
    }

    @Test
    fun `should return 422 while updating invalid plan`() {
        Given {
            port(8080)
            header("Content-Type", "application/vnd.smtm.v1+json")
            header("Accept", "application/vnd.smtm.v1+json")
            header("Authorization", "Bearer ${Environment.getAccessToken("owner-1")}")
            body(
                """
                {
                    "name": "September <2024>",
                    "period": {
                        "start": "2024-10-01",
                        "end": "2024-09-30"
                    },
                    "entries": [
                        {
                            "category-id": "smtm-cat-1",
                            "value": {
                                "amount": 37959,
                                "currency": "PLN"
                            }
                        },
                        {
                            "category-id": "smtm-cat-2",
                            "value": {
                                "amount": 600000,
                                "currency": "PLN"
                            }
                        },
                        {
                            "category-id": "smtm-cat-99",
                            "value": {
                                "amount": 100000,
                                "currency": "PLN"
                            }
                        }
                    ]
                }
            """.trimIndent()
            )
        } When {
            put("/plans/plan-1")
        } Then {
            statusCode(422)
            header("Content-Type", "application/problem+json")
            body("type", equalTo("https://api.smtm.com/problems/constraint-violations"))
            body("title", equalTo("Provided resource is not valid"))
            body("violations[0].path", equalTo("name"))
            body("violations[0].code", equalTo("ILLEGAL_CHARACTERS"))
            body("violations[0].parameters.illegal-characters", equalTo("<, >"))
            body("violations[1].path", equalTo("period"))
            body("violations[1].code", equalTo("INVALID"))
            body("violations[2].path", equalTo("entries/3/category"))
            body("violations[2].code", equalTo("UNKNOWN"))
        }
    }
}
