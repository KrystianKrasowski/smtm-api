package com.smtm.infrastructure.persistence.refresh.tokens

import org.springframework.data.repository.CrudRepository

interface DbRefreshTokensRepository : CrudRepository<RefreshToken, String> {

    fun findByIdAndSubject(id: String, subject: Long): RefreshToken?

    fun findBySubject(subject: Long): RefreshToken?
}
