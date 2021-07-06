package com.smtm.security.spi

interface RefreshTokensRepository {

    fun save(subject: Long, tokenId: String)

    fun exists(subject: Long, tokenId: String): Boolean
}
