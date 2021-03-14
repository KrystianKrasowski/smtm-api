package com.smtm.transactions

import com.smtm.transactions.spi.FakeCategoriesRepository

class World {

    val categoryRepository = FakeCategoriesRepository()
}
