package com.smtm.application.security

import com.smtm.security.api.Authorization
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthorizationFilter(authenticationManager: AuthenticationManager, private val authorization: Authorization) :
    BasicAuthenticationFilter(authenticationManager) {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        request.extractToken()
            ?.authorize()
            ?.store()

        chain.doFilter(request, response)
    }

    private fun HttpServletRequest.extractToken() = getHeader("Authorization")
        ?.takeIf { it.startsWith("Bearer ") }
        ?.replace("Bearer ", "")

    private fun String.authorize() = authorization
        .authorize(this)
        ?.let { UsernamePasswordAuthenticationToken(it.userId, null, ArrayList()) }

    private fun UsernamePasswordAuthenticationToken.store() {
        SecurityContextHolder.getContext()
            .authentication = this
    }
}
