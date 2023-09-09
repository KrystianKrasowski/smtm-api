package com.smtm.infrastructure.persistence

import javax.sql.DataSource
import org.springframework.jdbc.core.JdbcTemplate

fun DataSource.runSql(query: String) {
    JdbcTemplate(this).execute(query)
}
