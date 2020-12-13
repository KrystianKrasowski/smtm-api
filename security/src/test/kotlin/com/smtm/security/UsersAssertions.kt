package com.smtm.security

import com.smtm.security.registration.UserProfile
import com.smtm.security.registration.UserProfileAssert

fun assertThat(userProfile: UserProfile?) = UserProfileAssert(userProfile)
