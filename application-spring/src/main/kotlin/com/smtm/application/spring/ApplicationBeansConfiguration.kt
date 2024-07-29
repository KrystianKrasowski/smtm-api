package com.smtm.application.spring

import com.smtm.api.LinkFactory
import com.smtm.core.api.CategoriesApi
import com.smtm.core.api.PlansApi
import com.smtm.core.api.PlansQueries
import com.smtm.core.domain.OwnerId
import com.smtm.core.domain.ownerIdOf
import com.smtm.core.spi.CategoriesRepository
import com.smtm.infrastructure.persistence.CategoriesRepositoryJdbcAdapter
import com.smtm.infrastructure.persistence.PlansRepositoryJdbcAdapter
import javax.sql.DataSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Clock

@Configuration
class ApplicationBeansConfiguration {

    @Bean
    fun ownerIdProvider(): () -> OwnerId = { ownerIdOf(1) }

    @Bean
    fun linkFactory(): LinkFactory =
        LinkFactory("http", "localhost", 8080)

    @Bean
    fun clock(): Clock =
        Clock.systemUTC()

    @Bean
    fun categoriesApi(repository: CategoriesRepository): CategoriesApi =
        CategoriesApi.create(repository)

    @Bean
    fun plansApi(categoriesRepository: CategoriesRepository, plansRepository: PlansRepositoryJdbcAdapter): PlansApi =
        PlansApi.create(plansRepository)

    @Bean
    fun plansQueries(repository: PlansRepositoryJdbcAdapter): PlansQueries =
        repository

    @Bean
    fun categoriesRepository(dataSource: DataSource): CategoriesRepository =
        CategoriesRepositoryJdbcAdapter(dataSource)

    @Bean
    fun plansRepositoryJdbcAdapter(dataSource: DataSource, clock: Clock): PlansRepositoryJdbcAdapter =
        PlansRepositoryJdbcAdapter(dataSource, clock)
}
