package com.smtm.application.users.endpoint.v1;

import com.smtm.users.registration.UserProfile;

public class UserProfileDto {

    private final long id;
    private final String email;

    public static UserProfileDto of(UserProfile.Valid createdUser) {
        return of(createdUser.getId(), createdUser.getEmail().toString());
    }

    public static UserProfileDto of(long id, String email) {
        return new UserProfileDto(id, email);
    }

    private UserProfileDto(long id, String email) {
        this.id = id;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserProfileDto that = (UserProfileDto) o;

        if (id != that.id) return false;
        return email.equals(that.email);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + email.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "UserProfileDto{" +
            "id=" + id +
            ", email='" + email + '\'' +
            '}';
    }
}
