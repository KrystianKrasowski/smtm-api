package com.smtm.application.security.v1

import com.smtm.application.common.extensions.toMediaType
import com.smtm.security.registration.UserProfile
import org.springframework.hateoas.Link
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.http.ResponseEntity

data class UserProfileDto(val id: Long, val email: String) : RepresentationModel<UserProfileDto>() {

    fun toResponse201(): ResponseEntity<UserProfileDto> = ResponseEntity.created(getRequiredLink("self").toUri())
        .contentType(MediaTypeValue.toMediaType())
        .body(this)

    companion object {

        const val MediaTypeValue = "application/smtm.user-profile.v1+json"
    }
}

private val UserProfile.Valid.selfLink: Link
    get() = linkTo(methodOn(UsersController::class.java).getUser(id)).withSelfRel()

fun userProfileDtoOf(createdUser: UserProfile.Valid): UserProfileDto = UserProfileDto(createdUser.id, createdUser.email.toString())
    .add(createdUser.selfLink)
