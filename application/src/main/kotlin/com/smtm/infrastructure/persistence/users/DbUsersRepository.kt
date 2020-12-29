package com.smtm.infrastructure.persistence.users

import org.springframework.data.repository.CrudRepository

interface DbUsersRepository : CrudRepository<User, Long> {

    fun findByEmail(email: String): User?
}
