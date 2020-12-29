package com.smtm.infrastructure.persistence.users

import com.smtm.security.registration.EmailAddress
import com.smtm.security.registration.emailAddressOf
import com.smtm.security.registration.validUserProfileOf
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*

@Entity
@Table(name = "users")
data class User(
    val email: String,
    private val password: String,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null
) : UserDetails {

    override fun getAuthorities() = arrayOf(SimpleGrantedAuthority("USER")).toMutableList()

    override fun isEnabled() = true

    override fun getUsername() = email

    override fun isCredentialsNonExpired() = true

    override fun getPassword() = password

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    fun toUserProfile() = id
        ?.let { validUserProfileOf(it, emailAddressOf(email)) }
        ?: error("Cannot create valid user profile from entity before it is persited")
}

fun userOf(email: EmailAddress, password: String) = User(email.toString(), password)
