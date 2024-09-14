package com.smtm.api

import java.sql.Connection
import java.sql.DriverManager

class Database(private val connection: Connection) {

    fun disconnect() {
        connection.close()
    }

    fun runSql(sql: String) {
        connection
            .run { createStatement() }
            .use { it.execute(sql) }
    }

    companion object {

        fun connect(url: String, user: String, password: String): Database =
            Database(DriverManager.getConnection(url, user, password))
    }
}
