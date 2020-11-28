package com.smtm.application.users.infrastructure;

import org.jetbrains.annotations.NotNull;
import com.smtm.users.registration.Password;
import com.smtm.users.registration.UserProfile;
import com.smtm.users.spi.UsersRepository;

public class JpaUsersRepository implements UsersRepository {

    @NotNull
    @Override
    public UserProfile register(@NotNull String email, @NotNull Password password) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean isRegistered(@NotNull String email) {
        throw new IllegalStateException("Not implemented yet");
    }
}
