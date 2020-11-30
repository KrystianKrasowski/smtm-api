package com.smtm.application.users.infrastructure;

import javax.persistence.*;
import com.smtm.users.registration.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String email;

    @Column
    private String password;

    public static User of(EmailAddress email, Password password) {
        User user = new User();
        user.setEmail(email.toString());
        user.setPassword(password.getValue());
        return user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserProfile toUserProfile() {
        return UserProfileKt.validUserProfileOf(id, EmailAddressKt.emailAddressOf(email));
    }
}
