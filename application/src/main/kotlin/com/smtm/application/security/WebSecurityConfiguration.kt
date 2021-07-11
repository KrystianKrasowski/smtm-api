package com.smtm.application.security

import com.smtm.security.api.Authorization
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@EnableWebSecurity
class WebSecurityConfiguration(private val authorization: Authorization) : WebSecurityConfigurerAdapter() {

    override fun configure(web: WebSecurity) {
        web.ignoring()
            .antMatchers(HttpMethod.POST, "/security/users")
            .antMatchers(HttpMethod.POST, "/security/tokens")
    }

    override fun configure(http: HttpSecurity) {
        http.cors()
        http.csrf()
            .disable()

        http.authorizeRequests()
            .anyRequest()
            .authenticated()
            .and()
            .addFilter(JwtAuthorizationFilter(authenticationManager(), authorization))
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }
}

@Configuration
@EnableWebMvc
@Profile("development")
class DevelopmentWebMvcConfiguration: WebMvcConfigurer {

    override fun addCorsMappings(registry: CorsRegistry) {
        registry
            .addMapping("/**")
    }
}
