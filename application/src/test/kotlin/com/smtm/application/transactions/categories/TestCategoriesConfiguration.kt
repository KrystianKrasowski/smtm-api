package com.smtm.application.transactions.categories

import com.smtm.application.transactions.categories.v1.CategoriesController
import com.smtm.application.transactions.categories.v1.CategoriesRegister
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TestCategoriesConfiguration {

    @Bean
    fun categoriesRegister(): CategoriesRegister = FakeCategoriesRegister()

    @Bean
    fun categoriesController(categoriesRegister: CategoriesRegister): CategoriesController = CategoriesController(categoriesRegister)
}
