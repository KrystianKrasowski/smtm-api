package com.smtm.application.security.v1

import com.smtm.security.registration.UserProfile.Valid

data class UserProfileDto(val id: Long, val email: String) {

    companion object {

        const val MediaTypeValue = "application/smtm.user-profile.v1+json"
    }
}

fun userProfileDtoOf(createdUser: Valid) = userProfileDtoOf(createdUser.id, createdUser.email.toString())

fun userProfileDtoOf(id: Long, email: String) = UserProfileDto(id, email)
