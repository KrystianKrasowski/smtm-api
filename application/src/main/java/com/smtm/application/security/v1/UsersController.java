package com.smtm.application.security.v1;

import java.util.Optional;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.smtm.security.api.UserRegistration;

@RestController
@RequestMapping(path = "/security/users")
public class UsersController {

    private final UserRegistration userRegistration;

    public UsersController(UserRegistration userRegistration) {
        this.userRegistration = userRegistration;
    }

    @GetMapping(
        value = "/{id}",
        consumes = MediaTypes.USER_PROFILE_VALUE,
        produces = MediaTypes.USER_PROFILE_VALUE
    )
    public ResponseEntity<EntityModel<UserProfileDto>> getUser(@PathVariable("id") long id) {
        return null;
    }

    @PostMapping(
        consumes = MediaTypes.CREDENTIALS_VALUE,
        produces = MediaTypes.USER_PROFILE_VALUE
    )
    public ResponseEntity<?> registerUser(@RequestBody CredentialsDto user) {
        return Optional.of(userRegistration.register(user.getEmail(), user.getPassword()))
            .map(UserRegistrationResponseFactory::getFactoryFor)
            .map(UserRegistrationResponseFactory::create)
            .orElseThrow(() -> new IllegalStateException("Cannot register user"));
    }
}
