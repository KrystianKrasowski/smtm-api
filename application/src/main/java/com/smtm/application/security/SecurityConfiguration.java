package com.smtm.application.security;

import java.time.Clock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.smtm.application.security.users.infrastructure.BCryptPasswordEncryptionAdapter;
import com.smtm.application.security.users.infrastructure.DbUsersRepository;
import com.smtm.application.security.users.infrastructure.DbUsersRepositoryAdapter;
import com.smtm.security.api.Authentication;
import com.smtm.security.api.Authorization;
import com.smtm.security.api.UserRegistration;
import com.smtm.security.authentication.AuthenticationImplKt;
import com.smtm.security.authorization.AuthorizationImplKt;
import com.smtm.security.registration.UserRegistrationImplKt;
import com.smtm.security.spi.PasswordEncryption;
import com.smtm.security.spi.UsersRepository;

@Configuration
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UsersRepository usersRepository(DbUsersRepository dbUsersRepository, PasswordEncoder passwordEncoder) {
        return new DbUsersRepositoryAdapter(dbUsersRepository, passwordEncoder);
    }

    @Bean
    public PasswordEncryption passwordEncryption(PasswordEncoder passwordEncoder) {
        return new BCryptPasswordEncryptionAdapter(passwordEncoder);
    }

    @Bean
    public UserRegistration userRegistration(UsersRepository usersRepository, PasswordEncryption passwordEncryption) {
        return UserRegistrationImplKt.userRegistrationOf(usersRepository, passwordEncryption);
    }

    @Bean
    public Authentication tokenGenerator(UsersRepository usersRepository,
                                         @Value("${smtm.security.jwt.secret}") String secret,
                                         @Value("${smtm.security.jwt.validity}") Integer validityTime,
                                         Clock clock) {
        return AuthenticationImplKt.authenticationOf(usersRepository, secret, validityTime, clock);
    }

    @Bean
    public Authorization authorization(@Value("${smtm.security.jwt.secret}") String secret) {
        return AuthorizationImplKt.authorizationOf(secret);
    }
}
