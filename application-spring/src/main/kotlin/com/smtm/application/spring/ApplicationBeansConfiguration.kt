package com.smtm.application.spring

import com.smtm.application.repository.CategoriesRepository
import com.smtm.application.service.CategoriesService
import com.smtm.application.spring.infrastructure.storage.CategoriesRepositoryAdapter
import com.smtm.application.spring.infrastructure.storage.CategorySetsJpaRepository
import com.smtm.application.spring.infrastructure.storage.InMemoryCategoriesRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ApplicationBeansConfiguration {

    @Bean
    fun categoriesService(repository: CategoriesRepository): CategoriesService {
        return CategoriesService(repository)
    }

    @Bean
    fun categoriesRepository(jpaRepository: CategorySetsJpaRepository): CategoriesRepository {
        return CategoriesRepositoryAdapter(jpaRepository)
    }
}