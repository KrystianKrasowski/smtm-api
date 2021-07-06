package com.smtm.security.assertions

import com.smtm.security.registration.UserProfile
import com.smtm.security.spi.FakeRefreshTokensRepository

fun assertThat(userProfile: UserProfile?) = UserProfileAssert.assertThat(userProfile)

fun assertThat(repository: FakeRefreshTokensRepository) = RefreshTokenRepositoryAssert.assertThat(repository)
