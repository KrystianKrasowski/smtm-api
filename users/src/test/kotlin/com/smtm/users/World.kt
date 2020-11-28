package com.smtm.users

import com.smtm.users.spi.FakePasswordEncryption
import com.smtm.users.spi.FakeUserRepository

class World {

    var userRepository = FakeUserRepository()
    var passwordEncryption = FakePasswordEncryption()
}
