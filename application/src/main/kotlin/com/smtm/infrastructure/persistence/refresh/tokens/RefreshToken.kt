package com.smtm.infrastructure.persistence.refresh.tokens

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "refresh_tokens")
data class RefreshToken(
    @Id @Column(name = "user_id") val subject: Long,
    @Column(name = "token_id") var id: String
)
