package com.smtm.infrastructure.persistence

import org.springframework.jdbc.core.JdbcOperations
import org.springframework.jdbc.core.PreparedStatementCreator
import org.springframework.jdbc.support.GeneratedKeyHolder

internal fun JdbcOperations.insertAndGetId(
    psc: PreparedStatementCreator,
    idKeyName: String = "id",
    generatedKeyHolder: GeneratedKeyHolder = GeneratedKeyHolder()
): Long =
    update(psc, generatedKeyHolder)
        .let { generatedKeyHolder.extractLongId(idKeyName) }
        ?: error("There is no ID generated")

private fun GeneratedKeyHolder.extractLongId(keyName: String): Long? =
    keys
        ?.get(keyName)
        ?.toString()
        ?.toLong()
