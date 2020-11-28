package com.smtm.users

import com.smtm.users.registration.UserProfile
import com.smtm.users.registration.UserProfileAssert

fun assertThat(userProfile: UserProfile?) = UserProfileAssert(userProfile)
