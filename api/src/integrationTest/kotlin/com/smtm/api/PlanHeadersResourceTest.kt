package com.smtm.api

import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class PlanHeadersResourceTest {

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
        Environment.runSql("""
            INSERT INTO plans
            (id, owner_id, version, name, start, "end") 
            VALUES 
            ('plan-1', 'owner-1', 1, 'September 2024', '2024-09-01', '2024-09-30'),
            ('plan-2', 'owner-1', 1, 'October 2024', '2024-10-01', '2024-10-31'),
            ('plan-3', 'owner-1', 1, 'November 2024', '2024-11-01', '2024-11-30')
        """.trimIndent())
    }

    @AfterEach
    fun afterEach() {
        Environment.runSql("DELETE FROM plans CASCADE")
    }

    @Test
    fun `should return all plan headers`() {
        Given {
            port(8080)
            header("Content-Type", "application/vnd.smtm.v1+json")
            header("Accept", "application/vnd.smtm.v1+json")
            header("Authorization", "Bearer ${Environment.getAccessToken("owner-1")}")
        } When {
            get("/plan-headers")
        } Then {
            statusCode(200)
            header("Content-Type", "application/vnd.smtm.v1+json")
            body("_links.self.href", equalTo("http://localhost:8080/plan-headers"))
            body("count", equalTo(3))
            body("total", equalTo(3))
            body("_embedded.plan-headers[0]._links.self.href", equalTo("http://localhost:8080/plans/plan-3"))
            body("_embedded.plan-headers[0].id", equalTo("plan-3"))
            body("_embedded.plan-headers[0].name", equalTo("November 2024"))
            body("_embedded.plan-headers[0].period.start", equalTo("2024-11-01"))
            body("_embedded.plan-headers[0].period.end", equalTo("2024-11-30"))
            body("_embedded.plan-headers[1]._links.self.href", equalTo("http://localhost:8080/plans/plan-2"))
            body("_embedded.plan-headers[1].id", equalTo("plan-2"))
            body("_embedded.plan-headers[1].name", equalTo("October 2024"))
            body("_embedded.plan-headers[1].period.start", equalTo("2024-10-01"))
            body("_embedded.plan-headers[1].period.end", equalTo("2024-10-31"))
            body("_embedded.plan-headers[2]._links.self.href", equalTo("http://localhost:8080/plans/plan-1"))
            body("_embedded.plan-headers[2].id", equalTo("plan-1"))
            body("_embedded.plan-headers[2].name", equalTo("September 2024"))
            body("_embedded.plan-headers[2].period.start", equalTo("2024-09-01"))
            body("_embedded.plan-headers[2].period.end", equalTo("2024-09-30"))
        }
    }

    @Test
    fun `should return plan headers by matching date`() {
        Given {
            port(8080)
            header("Content-Type", "application/vnd.smtm.v1+json")
            header("Accept", "application/vnd.smtm.v1+json")
            header("Authorization", "Bearer ${Environment.getAccessToken("owner-1")}")
        } When {
            queryParam("matching-date", "2024-09-14")
            get("/plan-headers")
        } Then {
            statusCode(200)
            header("Content-Type", "application/vnd.smtm.v1+json")
            body("_links.self.href", equalTo("http://localhost:8080/plan-headers"))
            body("count", equalTo(1))
            body("total", equalTo(1))
            body("_embedded.plan-headers[0]._links.self.href", equalTo("http://localhost:8080/plans/plan-1"))
            body("_embedded.plan-headers[0].id", equalTo("plan-1"))
            body("_embedded.plan-headers[0].name", equalTo("September 2024"))
            body("_embedded.plan-headers[0].period.start", equalTo("2024-09-01"))
            body("_embedded.plan-headers[0].period.end", equalTo("2024-09-30"))
        }
    }
}
