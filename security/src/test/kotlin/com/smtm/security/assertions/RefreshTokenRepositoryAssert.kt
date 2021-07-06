package com.smtm.security.assertions

import com.smtm.security.spi.FakeRefreshTokensRepository
import org.assertj.core.api.AbstractAssert

class RefreshTokenRepositoryAssert private constructor(repository: FakeRefreshTokensRepository) :
    AbstractAssert<RefreshTokenRepositoryAssert, FakeRefreshTokensRepository>(repository, RefreshTokenRepositoryAssert::class.java) {

    fun hasEntry(subject: Long, tokenId: String): RefreshTokenRepositoryAssert {
        isNotNull
        if (!actual.exists(subject, tokenId)) {
            failWithMessage("Expected refresh token $tokenId to be stored for $subject subject")
        }
        return myself
    }

    companion object {

        fun assertThat(repository: FakeRefreshTokensRepository) = RefreshTokenRepositoryAssert(repository)
    }
}
