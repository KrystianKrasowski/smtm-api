package com.smtm.application.users;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.smtm.application.users.infrastructure.BCryptPasswordEncryptionAdapter;
import com.smtm.application.users.infrastructure.DbUsersRepositoryAdapter;
import com.smtm.application.users.infrastructure.DbUsersRepository;
import com.smtm.users.api.UserRegistration;
import com.smtm.users.registration.UserRegistrationImplKt;
import com.smtm.users.spi.PasswordEncryption;
import com.smtm.users.spi.UsersRepository;

@Configuration
public class UsersConfiguration {

    @Bean
    public UsersRepository usersRepository(DbUsersRepository usersRepositoryDb) {
        return new DbUsersRepositoryAdapter(usersRepositoryDb);
    }

    @Bean
    public PasswordEncryption passwordEncryption() {
        return new BCryptPasswordEncryptionAdapter();
    }

    @Bean
    public UserRegistration userRegistration(UsersRepository usersRepository, PasswordEncryption passwordEncryption) {
        return UserRegistrationImplKt.userRegistrationOf(usersRepository, passwordEncryption);
    }
}
