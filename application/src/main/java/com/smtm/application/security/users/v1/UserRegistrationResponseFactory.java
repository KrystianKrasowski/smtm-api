package com.smtm.application.security.users.v1;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import com.smtm.application.validation.v1.ConstraintViolationsDto;
import com.smtm.application.validation.v1.ValidationMediaTypes;
import com.smtm.users.registration.UserProfile;

abstract class UserRegistrationResponseFactory<T extends UserProfile> {

    T userProfile;

    UserRegistrationResponseFactory(T userProfile) {
        this.userProfile = userProfile;
    }

    static <T extends UserProfile> UserRegistrationResponseFactory<?> getFactoryFor(T userProfile) {
        if (userProfile instanceof UserProfile.Valid) {
            return new UserProfileDtoFactory((UserProfile.Valid) userProfile);
        }

        if (userProfile instanceof UserProfile.Invalid) {
            return new ConstraintsViolationsDtoFactory((UserProfile.Invalid) userProfile);
        }

        throw new IllegalArgumentException("Unsupported result type");
    }

    abstract ResponseEntity<?> create();

    static class UserProfileDtoFactory extends UserRegistrationResponseFactory<UserProfile.Valid> {

        UserProfileDtoFactory(UserProfile.Valid userProfile) {
            super(userProfile);
        }

        @Override
        public ResponseEntity<?> create() {
            UserProfileDto user = UserProfileDto.of(userProfile);
            EntityModel<UserProfileDto> model = createUserProfileRepresentation(user);
            return ResponseEntity
                .created(linkTo(methodOn(UsersController.class).getUser(user.getId())).toUri())
                .contentType(UserMediaTypes.USER_PROFILE)
                .body(model);
        }

        private EntityModel<UserProfileDto> createUserProfileRepresentation(UserProfileDto user) {
            return EntityModel.of(user)
                .add(linkTo(methodOn(UsersController.class).getUser(user.getId())).withSelfRel());
        }
    }

    static class ConstraintsViolationsDtoFactory extends UserRegistrationResponseFactory<UserProfile.Invalid> {

        ConstraintsViolationsDtoFactory(UserProfile.Invalid userProfile) {
            super(userProfile);
        }

        @Override
        public ResponseEntity<?> create() {
            ConstraintViolationsDto violationsDto = ConstraintViolationsDto.of(userProfile);
            return ResponseEntity
                .badRequest()
                .contentType(ValidationMediaTypes.CONSTRAINT_VIOLATION)
                .body(violationsDto);
        }
    }
}
