package com.smtm.application.categories

import com.smtm.application.categories.v1.CategoriesController
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TestCategoriesConfiguration {

    @Bean
    fun categoriesController(): CategoriesController {
        return CategoriesController()
    }
}
