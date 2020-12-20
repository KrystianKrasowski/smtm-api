package com.smtm.application.security;

import java.time.Clock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.smtm.application.security.users.infrastructure.BCryptPasswordEncryptionAdapter;
import com.smtm.application.security.users.infrastructure.DbUsersRepository;
import com.smtm.application.security.users.infrastructure.DbUsersRepositoryAdapter;
import com.smtm.security.api.Authentication;
import com.smtm.security.api.UserRegistration;
import com.smtm.security.registration.UserRegistrationImplKt;
import com.smtm.security.spi.PasswordEncryption;
import com.smtm.security.spi.UsersRepository;
import com.smtm.security.authentication.TokenGeneratorImplKt;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
            .antMatchers(HttpMethod.POST, "/security/users")
            .antMatchers(HttpMethod.POST, "/security/token");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .anyRequest()
            .authenticated()
            .and()
            .httpBasic()
            .realmName("Smtm")
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UsersRepository usersRepository(DbUsersRepository dbUsersRepository) {
        return new DbUsersRepositoryAdapter(dbUsersRepository);
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
    public Authentication tokenGenerator(@Value("${smtm.security.jwt.secret}") String secret,
                                         @Value("${smtm.security.jwt.validity}") Integer validityTime,
                                         Clock clock) {
        return TokenGeneratorImplKt.tokenGeneratorOf(secret, validityTime, clock);
    }
}
