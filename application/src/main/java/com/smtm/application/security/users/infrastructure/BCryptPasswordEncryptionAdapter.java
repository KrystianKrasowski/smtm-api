package com.smtm.application.security.users.infrastructure;

import org.jetbrains.annotations.NotNull;
import org.mindrot.jbcrypt.BCrypt;
import com.smtm.users.registration.Password;
import com.smtm.users.registration.PasswordKt;
import com.smtm.users.spi.PasswordEncryption;

public class BCryptPasswordEncryptionAdapter implements PasswordEncryption {

    @NotNull
    @Override
    public Password encrypt(@NotNull String value) {
        String encrypted = BCrypt.hashpw(value, BCrypt.gensalt());
        return PasswordKt.passwordOf(encrypted);
    }
}
