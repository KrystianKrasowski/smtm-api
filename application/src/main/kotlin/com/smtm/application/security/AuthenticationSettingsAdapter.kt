package com.smtm.application.security

import com.smtm.security.spi.AuthenticationSettings

class AuthenticationSettingsAdapter(override val secret: String,
                                    override val accessTokenValidTime: Int,
                                    override val refreshTokenValidTime: Int) : AuthenticationSettings
