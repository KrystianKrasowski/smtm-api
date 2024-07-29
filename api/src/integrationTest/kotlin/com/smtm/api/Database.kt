package com.smtm.api

import java.sql.Connection
import java.sql.DriverManager

private val DB_PORT = ENVIRONMENT["DB_PORT"]
private val DB_PASSWORD = ENVIRONMENT["DB_PASSWORD"]
private val DB_USER = ENVIRONMENT["DB_USER"]
private val DB_NAME = ENVIRONMENT["DB_NAME"]
private val DB_URL = "jdbc:postgresql://localhost:$DB_PORT/$DB_NAME"

class Database(private val connection: Connection) {

    fun disconnect() {
        connection.close()
    }

    fun runSql(sql: String) {
        connection
            .createStatement()
            .use { it.execute(sql) }
    }

    companion object {

        fun connect(): Database =
            Database(DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD))
    }
}
