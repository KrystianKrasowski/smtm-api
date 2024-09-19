package com.smtm.application.spring

import com.smtm.api.LinkFactory
import com.smtm.core.api.CategoriesApi
import com.smtm.core.api.PlansQueries
import com.smtm.core.domain.OwnerId
import com.smtm.core.spi.CategoriesRepository
import com.smtm.infrastructure.adapters.CategoriesRepositoryAdapter
import com.smtm.infrastructure.adapters.PlansAdapter
import javax.sql.DataSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Clock

@Configuration
class ApplicationBeansConfiguration {

    @Bean
    fun ownerIdProvider(): () -> OwnerId = { OwnerId.of(1) }

    @Bean
    fun linkFactory(): LinkFactory =
        LinkFactory("http", "localhost", 8080)

    @Bean
    fun clock(): Clock =
        Clock.systemUTC()

    @Bean
    fun categoriesApi(repository: CategoriesRepositoryAdapter): CategoriesApi =
        CategoriesApi.of(repository)

    @Bean
    fun plansQueries(plansAdapter: PlansAdapter): PlansQueries =
        plansAdapter

    // Adapters
    @Bean
    fun categoriesRepositoryAdapter(dataSource: DataSource): CategoriesRepositoryAdapter =
        CategoriesRepositoryAdapter(dataSource)

    @Bean
    fun plansAdapter(dataSource: DataSource): PlansAdapter =
        PlansAdapter(dataSource)
}
