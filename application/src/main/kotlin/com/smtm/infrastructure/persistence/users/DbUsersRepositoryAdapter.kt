package com.smtm.infrastructure.persistence.users

import com.smtm.security.registration.EmailAddress
import com.smtm.security.registration.UnencryptedPassword
import com.smtm.security.registration.UserProfile
import com.smtm.security.registration.UserProfile.Valid
import com.smtm.security.spi.UsersRepository
import org.springframework.security.crypto.password.PasswordEncoder

class DbUsersRepositoryAdapter(private val usersRepository: DbUsersRepository, private val passwordEncoder: PasswordEncoder) : UsersRepository {

    override fun register(email: EmailAddress, password: UnencryptedPassword): UserProfile = userOf(email, password.encrypt())
        .also { usersRepository.save(it) }
        .toUserProfile()

    override fun isRegistered(email: EmailAddress): Boolean = usersRepository.findByEmail(email.toString()) != null

    override fun findAuthorized(email: EmailAddress, password: UnencryptedPassword): Valid? = usersRepository.findByEmail(email.toString())
        ?.takeIf { passwordEncoder.matches(password.toString(), it.password) }
        ?.toUserProfile()

    private fun UnencryptedPassword.encrypt() = passwordEncoder.encode(this.toString())
}
