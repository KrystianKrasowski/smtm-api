package com.smtm.application.security.users.infrastructure;

import org.jetbrains.annotations.NotNull;
import com.smtm.security.registration.EmailAddress;
import com.smtm.security.registration.Password;
import com.smtm.security.registration.UserProfile;
import com.smtm.security.spi.UsersRepository;

public class DbUsersRepositoryAdapter implements UsersRepository {

    private final DbUsersRepository usersRepositoryDb;

    public DbUsersRepositoryAdapter(DbUsersRepository usersRepositoryDb) {
        this.usersRepositoryDb = usersRepositoryDb;
    }

    @NotNull
    @Override
    public UserProfile register(@NotNull EmailAddress email, @NotNull Password password) {
        User user = User.of(email, password);
        usersRepositoryDb.save(user);
        return user.toUserProfile();
    }

    @Override
    public boolean isRegistered(@NotNull EmailAddress email) {
        return usersRepositoryDb.findByEmail(email.toString()) != null;
    }
}
