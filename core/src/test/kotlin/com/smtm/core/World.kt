package com.smtm.core

import com.smtm.core.domain.ownerIdOf
import com.smtm.core.spi.FakeCategoriesRepository
import com.smtm.core.spi.FakePlansRepository

class World {

    var ownerId = ownerIdOf(1)
    var categoriesRepository = FakeCategoriesRepository()
    var plansRepository = FakePlansRepository(categoriesRepository)
}
