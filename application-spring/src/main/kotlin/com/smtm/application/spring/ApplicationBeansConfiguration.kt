package com.smtm.application.spring

import com.smtm.application.LinkFactory
import com.smtm.application.api.CategoriesApi
import com.smtm.application.api.PlansApi
import com.smtm.application.api.PlansQueries
import com.smtm.application.domain.OwnerId
import com.smtm.application.domain.ownerIdOf
import com.smtm.application.spi.CategoriesRepository
import com.smtm.infrastructure.persistence.CategoriesRepositoryJdbcAdapter
import com.smtm.infrastructure.persistence.PlansRepositoryJdbcAdapter
import javax.sql.DataSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.transaction.support.TransactionOperations
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
        PlansApi.create(categoriesRepository, plansRepository)

    @Bean
    fun plansQueries(repository: PlansRepositoryJdbcAdapter): PlansQueries =
        repository

    @Bean
    fun categoriesRepository(dataSource: DataSource): CategoriesRepository =
        CategoriesRepositoryJdbcAdapter(dataSource)

    @Bean
    fun plansRepositoryJdbcAdapter(
        dataSource: DataSource,
        clock: Clock,
        jdbc: JdbcOperations,
        transactions: TransactionOperations
    ): PlansRepositoryJdbcAdapter =
        PlansRepositoryJdbcAdapter(dataSource, clock)
}
