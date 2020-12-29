package com.smtm.application.security.v1

import com.smtm.application.common.dto.ViolationsProblemDto
import com.smtm.security.api.UserRegistration
import com.smtm.security.registration.UserProfile
import org.springframework.hateoas.MediaTypes.HTTP_PROBLEM_DETAILS_JSON
import org.springframework.hateoas.mediatype.problem.Problem
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

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

private fun UserProfile.Valid.toResponse201() = userProfileDtoOf(this).toResponse201()

private fun UserProfile.Invalid.toResponse400() = Problem.create()
    .withTitle("Provided credentials violate some of the constraints")
    .withProperties(ViolationsProblemDto(violations))
    .toResponse400()

private fun Problem.toResponse400() = ResponseEntity.badRequest()
    .contentType(HTTP_PROBLEM_DETAILS_JSON)
    .body(this)
