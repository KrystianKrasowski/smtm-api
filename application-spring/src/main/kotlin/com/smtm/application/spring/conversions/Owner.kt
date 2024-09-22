package com.smtm.application.spring.conversions

import com.smtm.core.domain.OwnerId
import org.springframework.security.core.Authentication

object Owner {

    fun Authentication.toOwnerId(): OwnerId =
        OwnerId.of(name)
}
