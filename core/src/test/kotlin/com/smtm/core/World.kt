package com.smtm.core

import com.smtm.core.domain.OwnerId
import com.smtm.core.spi.FakeCategoriesRepository

class World {

    var ownerId = OwnerId.of(1)
    var categoriesRepository = FakeCategoriesRepository()
}
