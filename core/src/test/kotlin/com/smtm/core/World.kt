package com.smtm.core

import com.smtm.core.domain.OwnerId
import com.smtm.core.spi.CategoriesTestRepository
import com.smtm.core.spi.PlansTestRepository

class World {

    var ownerId = OwnerId.of("1")
    var categoriesRepository = CategoriesTestRepository()
    var plansRepository = PlansTestRepository()
}
