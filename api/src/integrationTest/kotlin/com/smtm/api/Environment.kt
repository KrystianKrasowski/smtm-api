package com.smtm.api

import com.tngtech.keycloakmock.api.KeycloakMock
import com.tngtech.keycloakmock.api.ServerConfig.aServerConfig
import com.tngtech.keycloakmock.api.TokenConfig.aTokenConfig
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.containers.wait.strategy.Wait
import java.io.File

object Environment {

    private lateinit var compose: DockerComposeContainer<*>
    private lateinit var database: Database
    private lateinit var authServer: KeycloakMock

    fun setUp() {
        compose = setUpCompose()
        database = setUpDatabase()
        authServer = setUpAuthServer()
    }

    fun runSql(query: String) {
        database.runSql(query)
    }

    fun tearDown() {
        compose.stop()
        database.disconnect()
        authServer.stop()
    }

    fun getAccessToken(subject: String): String =
        authServer.getAccessToken(aTokenConfig().withSubject(subject).build())

    private fun setUpCompose(): DockerComposeContainer<*> =
        DockerComposeContainer(File("../docker-compose.yml"))
            .withExposedService("application_1", 8080, Wait.forListeningPort())
            .withExposedService("db_1", 5432, Wait.forListeningPort())
            .withEnv(ENVIRONMENT)
            .withLogConsumer("application_1") { println(it.utf8String) }
            .apply { start() }

    private fun setUpDatabase(): Database =
        Database.connect(
            url = "jdbc:postgresql://localhost:${ENVIRONMENT["DB_PORT"]}/${ENVIRONMENT["DB_NAME"]}",
            user = ENVIRONMENT.getValue("DB_USER"),
            password = ENVIRONMENT.getValue("DB_PASSWORD"),
        )

    private fun setUpAuthServer(): KeycloakMock =
        KeycloakMock(
            aServerConfig()
                .withDefaultHostname("172.17.0.1")
                .withNoContextPath()
                .withPort(8084)
                .withDefaultRealm("smtm")
                .build()
        ).apply { start() }
}

private val ENVIRONMENT = mapOf(
    "DB_HOST" to "db",
    "DB_PORT" to "5432",
    "DB_NAME" to "smtm-it",
    "DB_USER" to "smtm-it",
    "DB_PASSWORD" to "smtm123",
    "CORS_ALLOWED_ORIGINS" to "*",
    "CORS_ALLOWED_METHODS" to "*",
    "OAUTH_ISSUER_URI" to "http://172.17.0.1:8084/realms/smtm"
)
