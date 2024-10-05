package com.smtm.application.spring

import com.smtm.api.LinkFactory
import com.smtm.core.api.CategoriesApi
import com.smtm.core.api.PlansApi
import com.smtm.core.api.PlansQueries
import com.smtm.core.api.WalletsApi
import com.smtm.core.domain.OwnerId
import com.smtm.infrastructure.adapters.CategoriesRepositoryJpaAdapter
import com.smtm.infrastructure.adapters.PlansRepositoryJpaAdapter
import com.smtm.infrastructure.adapters.WalletsRepositoryJpaAdapter
import jakarta.persistence.EntityManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.context.SecurityContextHolder
import java.time.Clock

@Configuration
class ApplicationBeansConfiguration {

    @Autowired
    private lateinit var entityManager: EntityManager

    @Bean
    fun ownerIdProvider(): () -> OwnerId =
        { SecurityContextHolder.getContext().authentication.name.let { OwnerId.of(it) } }

    @Bean
    fun linkFactory(): LinkFactory =
        LinkFactory("http", "localhost", 8080)

    @Bean
    fun clock(): Clock =
        Clock.systemUTC()

    @Bean
    fun categoriesApi(repository: CategoriesRepositoryJpaAdapter): CategoriesApi =
        CategoriesApi.of(repository)

    @Bean
    fun plansApi(
        categoriesRepository: CategoriesRepositoryJpaAdapter,
        plansRepository: PlansRepositoryJpaAdapter
        ): PlansApi =
        PlansApi.of(categoriesRepository, plansRepository)

    @Bean
    fun plansQueries(plansAdapter: PlansRepositoryJpaAdapter): PlansQueries =
        plansAdapter

    @Bean
    fun walletsApi(
        walletsRepositoryJpaAdapter: WalletsRepositoryJpaAdapter
    ): WalletsApi =
        WalletsApi.create(walletsRepositoryJpaAdapter)

    // Adapters
    @Bean
    fun categoriesRepositoryAdapter(
        ownerIdProvider: () -> OwnerId
    ): CategoriesRepositoryJpaAdapter =
        CategoriesRepositoryJpaAdapter(entityManager, ownerIdProvider)

    @Bean
    fun plansRepositoryJpaAdapter(ownerIdProvider: () -> OwnerId): PlansRepositoryJpaAdapter =
        PlansRepositoryJpaAdapter(entityManager, ownerIdProvider)

    @Bean
    fun walletsRepositoryJpaAdapter(ownerIdProvider: () -> OwnerId): WalletsRepositoryJpaAdapter =
        WalletsRepositoryJpaAdapter(entityManager, ownerIdProvider)
}
