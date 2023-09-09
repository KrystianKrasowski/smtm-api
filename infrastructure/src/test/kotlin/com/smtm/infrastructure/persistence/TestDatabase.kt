package com.smtm.infrastructure.persistence

import javax.sql.DataSource
import org.flywaydb.core.Flyway
import org.postgresql.ds.PGSimpleDataSource
import org.testcontainers.containers.PostgreSQLContainer

object TestDatabase {

    private val container: PostgreSQLContainer<*> = PostgreSQLContainer("postgres:15.3")
        .withDatabaseName("smtm_test")
        .withUsername("smtm_test")
        .withPassword("1234")

    fun setup(): DataSource =
        container
            .startIfNotRunning()
            .setupDataSource()
            .setupFlyway()

    private fun PostgreSQLContainer<*>.startIfNotRunning(): PostgreSQLContainer<*> =
        apply {
            if (!isRunning()) {
                start()
            }
        }

    private fun PostgreSQLContainer<*>.setupDataSource(): DataSource =
        PGSimpleDataSource()
            .also { it.setURL(getJdbcUrl()) }
            .also { it.user = username }
            .also { it.password = password }

    private fun DataSource.setupFlyway(): DataSource =
        Flyway.configure()
            .locations("/db/migration")
            .cleanDisabled(false)
            .dataSource(this)
            .let { Flyway(it) }
            .also { it.clean() }
            .also { it.migrate() }
            .let { this }
}
