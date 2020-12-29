package com.smtm.application.security.v1

import com.smtm.application.common.dto.ViolationsProblemDto
import com.smtm.application.common.extensions.toMediaType
import com.smtm.security.api.UserRegistration
import com.smtm.security.registration.UserProfile
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.Link
import org.springframework.hateoas.MediaTypes.HTTP_PROBLEM_DETAILS_JSON
import org.springframework.hateoas.mediatype.problem.Problem
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping(path = ["/security/users"])
class UsersController(private val userRegistration: UserRegistration) {

    @GetMapping(
            value = ["/{id}"],
            consumes = [UserProfileDto.MediaTypeValue],
            produces = [UserProfileDto.MediaTypeValue]
    )
    fun getUser(@PathVariable("id") id: Long): ResponseEntity<*> {
        TODO("Not implemented yet")
    }

    @PostMapping(
            consumes = [CredentialsDto.MediaTypeValue],
            produces = [UserProfileDto.MediaTypeValue]
    )
    fun registerUser(@RequestBody credentials: CredentialsDto): ResponseEntity<*> = when (val profile = credentials.register()) {
        is UserProfile.Valid -> profile.toResponse201()
        is UserProfile.Invalid -> profile.toResponse400()
    }

    private fun CredentialsDto.register() = userRegistration.register(email, password)

}

private fun UserProfile.Valid.selfLink() = linkTo(methodOn(UsersController::class.java).getUser(id))

private fun UserProfile.Valid.toResponse201() = userProfileDtoOf(this)
        .toEntityModel(selfLink().withSelfRel())
        .toResponseEntity(selfLink().toUri())

private fun UserProfile.Invalid.toResponse400() = Problem.create()
        .withTitle("Provided credentials violate some of the constraints")
        .withProperties(ViolationsProblemDto(violations))
        .toResponseEntity()

private fun UserProfileDto.toEntityModel(link: Link) = EntityModel.of(this)
        .add(link)

private fun EntityModel<UserProfileDto>.toResponseEntity(location: URI) = ResponseEntity.created(location)
        .contentType(UserProfileDto.MediaTypeValue.toMediaType())
        .body(this)

private fun Problem.toResponseEntity() = ResponseEntity.badRequest()
        .contentType(HTTP_PROBLEM_DETAILS_JSON)
        .body(this)
