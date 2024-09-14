package com.smtm.core

import com.smtm.core.domain.ownerIdOf
import com.smtm.core.spi.FakeCategoriesRepository

class World {

    var ownerId = ownerIdOf(1)
    var categoriesRepository = FakeCategoriesRepository()
}
