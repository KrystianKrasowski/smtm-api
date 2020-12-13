package com.smtm.security

import com.smtm.security.spi.FakePasswordEncryption
import com.smtm.security.spi.FakeUserRepository

class World {

    var userRepository = FakeUserRepository()
    var passwordEncryption = FakePasswordEncryption()
}
