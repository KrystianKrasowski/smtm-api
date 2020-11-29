package com.smtm.application.users.infrastructure;

import org.jetbrains.annotations.NotNull;
import com.smtm.users.registration.Password;
import com.smtm.users.registration.UserProfile;
import com.smtm.users.spi.UsersRepository;

public class DbUsersRepositoryAdapter implements UsersRepository {

    private final DbUsersRepository usersRepositoryDb;

    public DbUsersRepositoryAdapter(DbUsersRepository usersRepositoryDb) {
        this.usersRepositoryDb = usersRepositoryDb;
    }

    @NotNull
    @Override
    public UserProfile register(@NotNull String email, @NotNull Password password) {
        User user = User.of(email, password);
        usersRepositoryDb.save(user);
        return user.toUserProfile();
    }

    @Override
    public boolean isRegistered(@NotNull String email) {
        return usersRepositoryDb.findByEmail(email) != null;
    }
}
