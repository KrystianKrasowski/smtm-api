package com.smtm.application

import com.smtm.application.domain.ownerIdOf
import com.smtm.application.spi.FakeCategoriesRepository

class World {

    var ownerId = ownerIdOf(1)
    var categoriesRepository = FakeCategoriesRepository()
}