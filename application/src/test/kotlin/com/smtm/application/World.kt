package com.smtm.application

import com.smtm.application.domain.ownerIdOf
import com.smtm.application.repository.InMemoryCategoriesRepository

class World {

    var ownerId = ownerIdOf(1)
    val categoriesRepository = InMemoryCategoriesRepository()
}