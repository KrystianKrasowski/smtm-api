package com.smtm.application.spring

import com.smtm.application.domain.OwnerId
import com.smtm.application.domain.ownerIdOf
import com.smtm.application.repository.CategoriesRepository
import com.smtm.application.service.CategoriesService
import com.smtm.application.spring.infrastructure.storage.CategoriesRepositoryAdapter
import com.smtm.application.spring.infrastructure.storage.CategorySetsJpaRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ApplicationBeansConfiguration {

    @Bean
    fun ownerIdProvider(): () -> OwnerId = { ownerIdOf(1) }

    @Bean
    fun categoriesService(repository: CategoriesRepository): CategoriesService {
        return CategoriesService(repository)
    }

    @Bean
    fun categoriesRepository(jpaRepository: CategorySetsJpaRepository): CategoriesRepository {
        return CategoriesRepositoryAdapter(jpaRepository)
    }
}