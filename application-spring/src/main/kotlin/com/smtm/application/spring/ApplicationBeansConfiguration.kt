package com.smtm.application.spring

import com.smtm.api.LinkFactory
import com.smtm.core.api.CategoriesApi
import com.smtm.core.api.PlansQueries
import com.smtm.core.domain.OwnerId
import com.smtm.infrastructure.adapters.CategoriesRepositoryJpaAdapter
import com.smtm.infrastructure.adapters.PlansAdapter
import jakarta.persistence.EntityManager
import javax.sql.DataSource
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
    fun plansQueries(plansAdapter: PlansAdapter): PlansQueries =
        plansAdapter

    // Adapters
    @Bean
    fun categoriesRepositoryAdapter(
        ownerIdProvider: () -> OwnerId
    ): CategoriesRepositoryJpaAdapter =
        CategoriesRepositoryJpaAdapter(entityManager, ownerIdProvider)

    @Bean
    fun plansAdapter(dataSource: DataSource): PlansAdapter =
        PlansAdapter(dataSource)
}
