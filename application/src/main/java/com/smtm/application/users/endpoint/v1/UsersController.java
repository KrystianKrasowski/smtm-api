package com.smtm.application.users.endpoint.v1;

import java.util.Optional;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.smtm.application.validation.v1.ValidationMediaTypes;
import com.smtm.users.api.UserRegistration;

@RestController
public class UsersController {

    private final UserRegistration userRegistration;

    public UsersController(UserRegistration userRegistration) {
        this.userRegistration = userRegistration;
    }

    @GetMapping(
        value = "/users/{id}",
        consumes = UserMediaTypes.USER_PROFILE_VALUE,
        produces = UserMediaTypes.USER_PROFILE_VALUE
    )
    public ResponseEntity<EntityModel<UserProfileDto>> getUser(@PathVariable("id") long id) {
        return null;
    }

    @PostMapping(
        value = "/users",
        consumes = UserMediaTypes.NEW_USER_VALUE,
        produces = {
            UserMediaTypes.USER_PROFILE_VALUE,
            ValidationMediaTypes.CONSTRAINT_VIOLATION_VALUE
        }
    )
    public ResponseEntity<?> registerUser(@RequestBody NewUserDto user) {
        return Optional.of(userRegistration.register(user.getEmail(), user.getPassword()))
            .map(UserRegistrationResponseFactory::getFactoryFor)
            .map(UserRegistrationResponseFactory::create)
            .orElseThrow(() -> new IllegalStateException("Cannot register user"));
    }
}
