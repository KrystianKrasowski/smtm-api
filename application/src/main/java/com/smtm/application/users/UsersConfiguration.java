package com.smtm.application.users;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.smtm.users.api.UserRegistration;
import com.smtm.users.registration.UserRegistrationImplKt;

@Configuration
public class UsersConfiguration {

    @Bean
    public UserRegistration userRegistration() {
        return UserRegistrationImplKt.userRegistrationOf();
    }
}
