package com.smtm.application.security.infrastructure;

import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.smtm.security.registration.EmailAddress;
import com.smtm.security.registration.UnencryptedPassword;
import com.smtm.security.registration.UserProfile;
import com.smtm.security.spi.UsersRepository;

public class DbUsersRepositoryAdapter implements UsersRepository {

    private final DbUsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public DbUsersRepositoryAdapter(DbUsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @NotNull
    @Override
    public UserProfile register(@NotNull EmailAddress email, @NotNull UnencryptedPassword password) {
        User user = User.of(email, passwordEncoder.encode(password.toString()));
        usersRepository.save(user);
        return user.toUserProfile();
    }

    @Override
    public boolean isRegistered(@NotNull EmailAddress email) {
        return usersRepository.findByEmail(email.toString()) != null;
    }

    @Nullable
    @Override
    public UserProfile.Valid findAuthorized(@NotNull EmailAddress email, @NotNull UnencryptedPassword password) {
        return Optional.ofNullable(usersRepository.findByEmail(email.toString()))
            .filter(user -> passwordEncoder.matches(password.toString(), user.getPassword()))
            .map(User::toUserProfile)
            .orElse(null);
    }
}
