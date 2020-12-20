package com.smtm.security

import com.smtm.security.spi.FakeUserRepository
import java.time.Clock

class World {

    var userRepository = FakeUserRepository()
    var tokenGenerationKey = "aaa"
    var tokenValidityTime = 900
    var clock = Clock.systemUTC()
}
