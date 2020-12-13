package com.smtm.application.security.users.infrastructure;

import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.smtm.users.registration.Password;
import com.smtm.users.registration.PasswordKt;
import com.smtm.users.spi.PasswordEncryption;

public class BCryptPasswordEncryptionAdapter implements PasswordEncryption {

    private final PasswordEncoder passwordEncoder;

    public BCryptPasswordEncryptionAdapter(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @NotNull
    @Override
    public Password encrypt(@NotNull String value) {
        return Optional.of(passwordEncoder.encode(value))
            .map(PasswordKt::passwordOf)
            .orElseThrow(() -> new IllegalStateException("Cannot encode password"));
    }
}
