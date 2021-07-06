package com.smtm.infrastructure.persistence.refresh.tokens

import com.smtm.security.spi.RefreshTokensRepository

open class RefreshTokensRepositoryAdapter(private val dbRefreshTokensRepository: DbRefreshTokensRepository) : RefreshTokensRepository {

    override fun save(subject: Long, tokenId: String) {
        fetchTokenOrCreateNew(subject)
            .apply { id = tokenId }
            .apply { dbRefreshTokensRepository.save(this) }
    }

    override fun exists(subject: Long, tokenId: String): Boolean {
        return dbRefreshTokensRepository.findByIdAndSubject(tokenId, subject) != null
    }

    private fun fetchTokenOrCreateNew(subject: Long) = dbRefreshTokensRepository
        .findBySubject(subject)
        ?: RefreshToken(subject, "")
}
