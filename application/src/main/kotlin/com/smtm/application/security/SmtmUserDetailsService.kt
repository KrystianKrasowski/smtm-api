package com.smtm.application.security

import com.smtm.infrastructure.persistence.users.DbUsersRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

// In order to make Spring Security magic work properly, it seems that this should be the only implementation of UserServiceDetails interface
// It cannot be exposed as @Bean method with parameters or I can't do it.
@Service
class SmtmUserDetailsService(private val dbUsersRepository: DbUsersRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails? = dbUsersRepository.findByEmail(username)
}
