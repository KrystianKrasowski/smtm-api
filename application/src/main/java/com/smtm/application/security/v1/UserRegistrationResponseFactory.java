package com.smtm.application.security.v1;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import com.smtm.application.common.dto.ViolationsProblemDto;
import com.smtm.security.registration.UserProfile;

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
            return new ProblemFactory((UserProfile.Invalid) userProfile);
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
                .created(WebMvcLinkBuilder.linkTo(methodOn(UsersController.class).getUser(user.getId())).toUri())
                .contentType(com.smtm.application.security.v1.MediaTypes.USER_PROFILE)
                .body(model);
        }

        private EntityModel<UserProfileDto> createUserProfileRepresentation(UserProfileDto user) {
            return EntityModel.of(user)
                .add(linkTo(methodOn(UsersController.class).getUser(user.getId())).withSelfRel());
        }
    }

    static class ProblemFactory extends UserRegistrationResponseFactory<UserProfile.Invalid> {

        ProblemFactory(UserProfile.Invalid userProfile) {
            super(userProfile);
        }

        @Override
        ResponseEntity<?> create() {
            Problem.ExtendedProblem<ViolationsProblemDto> problem = Problem.create()
                .withTitle("Provided credentials violate some of the constraints")
                .withProperties(new ViolationsProblemDto(userProfile.getViolations()));

            return ResponseEntity.badRequest()
                .contentType(MediaTypes.HTTP_PROBLEM_DETAILS_JSON)
                .body(problem);
        }
    }

}
