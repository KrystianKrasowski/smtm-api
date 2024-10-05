package com.smtm.core

import com.smtm.core.spi.CategoriesTestRepository
import com.smtm.core.spi.PlansTestRepository
import com.smtm.core.spi.WalletsTestRepository

class World {

    var categoriesRepository = CategoriesTestRepository()
    var walletsRepository = WalletsTestRepository()
    var plansRepository = PlansTestRepository()
}
