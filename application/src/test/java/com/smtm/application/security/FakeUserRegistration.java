package com.smtm.application.security;

import org.jetbrains.annotations.NotNull;
import com.smtm.security.api.UserRegistration;
import com.smtm.security.registration.EmailAddress;
import com.smtm.security.registration.UnencryptedPassword;
import com.smtm.security.registration.UserProfile;

class FakeUserRegistration implements UserRegistration {

    private UserProfile userProfile = null;

    @NotNull
    @Override
    public UserProfile register(@NotNull EmailAddress email, @NotNull UnencryptedPassword password) {
        if (userProfile == null) {
            return new UserProfile.Valid(1L, email);
        } else {
            return userProfile;
        }
    }

    void setUserProfile(UserProfile profile) {
        this.userProfile = profile;
    }
}
