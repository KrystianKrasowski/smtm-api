package com.smtm.application.users.endpoint.v1;

import org.jetbrains.annotations.NotNull;
import com.smtm.users.api.UserRegistration;
import com.smtm.users.registration.UnsecuredPassword;
import com.smtm.users.registration.UserProfile;
import com.smtm.users.registration.UserProfileKt;

public class FakeUserRegistration implements UserRegistration {

    private UserProfile userProfile = UserProfileKt.validUserProfileOf(0, "noname@example.com");

    @NotNull
    @Override
    public UserProfile register(@NotNull String email, @NotNull UnsecuredPassword password) {
        return userProfile;
    }

    void returns(UserProfile userProfile) {
        this.userProfile = userProfile;
    }
}
