package com.smtm.application.users.infrastructure;

import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.smtm.users.registration.Password;
import com.smtm.users.registration.PasswordKt;
import com.smtm.users.spi.PasswordEncryption;

public class BCryptPasswordEncryptionAdapter implements PasswordEncryption {

    @NotNull
    @Override
    public Password encrypt(@NotNull String value) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return PasswordKt.passwordOf(encoder.encode(value));
    }
}
