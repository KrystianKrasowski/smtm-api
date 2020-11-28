package com.smtm.application.users.infrastructure;

import org.jetbrains.annotations.NotNull;
import com.smtm.users.registration.Password;
import com.smtm.users.spi.PasswordEncryption;

public class BCryptPasswordEncryption implements PasswordEncryption {

    @NotNull
    @Override
    public Password encrypt(@NotNull String value) {
        throw new IllegalStateException("Not implemented yet");
    }
}
