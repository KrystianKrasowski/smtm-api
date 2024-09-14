package com.smtm.api

import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.containers.wait.strategy.Wait
import java.io.File

object Environment {

    private lateinit var compose: DockerComposeContainer<*>
    private lateinit var database: Database

    fun setUp() {
        compose = DockerComposeContainer(File("../docker-compose.yml"))
            .withExposedService("application_1", 8080, Wait.forListeningPort())
            .withExposedService("db_1", 5432, Wait.forListeningPort())
            .withEnv(ENVIRONMENT)
            .withLogConsumer("application_1") { frame -> println(frame.utf8String) }
            .apply { start() }

        try {
            database = Database.connect(
                url = "jdbc:postgresql://localhost:${ENVIRONMENT["DB_PORT"]}/${ENVIRONMENT["DB_NAME"]}",
                user = ENVIRONMENT.getValue("DB_USER"),
                password = ENVIRONMENT.getValue("DB_PASSWORD"),
            )
        } catch (e: Exception) {
            throw e
        }
    }

    fun runSql(query: String) {
        database.runSql(query)
    }

    fun tearDown() {
        compose.stop()
        database.disconnect()
    }
}

private val ENVIRONMENT = mapOf(
    "DB_HOST" to "db",
    "DB_PORT" to "5432",
    "DB_NAME" to "smtm-it",
    "DB_USER" to "smtm-it",
    "DB_PASSWORD" to "smtm123",
    "CORS_ALLOWED_ORIGINS" to "*",
    "CORS_ALLOWED_METHODS" to "*"
)
