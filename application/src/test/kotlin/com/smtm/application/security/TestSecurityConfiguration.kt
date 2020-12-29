package com.smtm.application.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.smtm.application.security.v1.TokenController;
import com.smtm.application.security.v1.UsersController;
import com.smtm.security.api.Authentication;
import com.smtm.security.api.Authorization;
import com.smtm.security.api.UserRegistration;

@Configuration
public class TestSecurityConfiguration {

    @Bean
    public UserRegistration userRegistration() {
        return new FakeUserRegistration();
    }

    @Bean
    public Authorization authorization() {
        return new FakeAuthorization();
    }

    @Bean
    public Authentication authentication(@Value("${smtm.security.jwt.secret}") String secret) {
        return new FakeAuthentication(secret);
    }

    @Bean
    public UsersController usersController(UserRegistration userRegistration) {
        return new UsersController(userRegistration);
    }

    @Bean
    public TokenController tokenController(Authentication authentication) {
        return new TokenController(authentication);
    }

    @Bean
    public TestProtectedResourceController testProtectedResourceController() {
        return new TestProtectedResourceController();
    }
}
