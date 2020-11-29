package com.smtm.application.users.endpoint.v1;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import com.smtm.application.validation.v1.ConstraintViolationsDto;
import com.smtm.application.validation.v1.ValidationMediaTypes;
import com.smtm.users.api.UserRegistration;
import com.smtm.users.registration.PasswordKt;
import com.smtm.users.registration.UserProfile;

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
        produces = UserMediaTypes.USER_PROFILE_VALUE
    )
    public ResponseEntity<?> registerUser() {
        UserProfile userProfile = userRegistration.register("not-important@gmail.com", PasswordKt.unsecuredPasswordOf("secret"));
        return createResponse(userProfile);
    }

    private ResponseEntity<?> createResponse(UserProfile userProfile) {
        if (userProfile instanceof UserProfile.Valid) {
            return createUserProfileResponse((UserProfile.Valid) userProfile);
        }

        if (userProfile instanceof UserProfile.Invalid) {
            return createConstraintViolationsResponse((UserProfile.Invalid) userProfile);
        }

        throw new IllegalArgumentException("Unsupported result type");
    }

    @NotNull
    private ResponseEntity<EntityModel<UserProfileDto>> createUserProfileResponse(UserProfile.Valid userProfile) {
        UserProfileDto user = UserProfileDto.of(userProfile);
        EntityModel<UserProfileDto> model = createUserProfileRepresentation(user);
        return ResponseEntity
            .created(linkTo(methodOn(UsersController.class).getUser(user.getId())).toUri())
            .contentType(UserMediaTypes.USER_PROFILE)
            .body(model);
    }

    @NotNull
    private ResponseEntity<ConstraintViolationsDto> createConstraintViolationsResponse(UserProfile.Invalid userProfile) {
        ConstraintViolationsDto violationsDto = ConstraintViolationsDto.of(userProfile);
        return ResponseEntity
            .badRequest()
            .contentType(ValidationMediaTypes.CONSTRAINT_VIOLATION)
            .body(violationsDto);
    }

    private EntityModel<UserProfileDto> createUserProfileRepresentation(UserProfileDto user) {
        return EntityModel.of(user)
            .add(linkTo(methodOn(UsersController.class).getUser(user.getId())).withSelfRel());
    }
}
