package com.smtm.application.spring

import com.smtm.application.LinkFactory
import com.smtm.application.api.CategoriesApi
import com.smtm.application.api.PlansQueries
import com.smtm.application.domain.OwnerId
import com.smtm.application.domain.ownerIdOf
import com.smtm.application.spi.CategoriesRepository
import com.smtm.application.spring.infrastructure.persistence.CategoriesRepositoryJdbcAdapter
import com.smtm.application.spring.infrastructure.persistence.PlansRepositoryJdbcAdapter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.TransactionOperations
import org.springframework.transaction.support.TransactionTemplate
import java.time.Clock
import javax.sql.DataSource

@Configuration
class ApplicationBeansConfiguration {

    @Bean
    fun ownerIdProvider(): () -> OwnerId = { ownerIdOf(1) }

    @Bean
    fun linkFactory(): LinkFactory {
        return LinkFactory("http", "localhost", 8080)
    }

    @Bean
    fun clock(): Clock {
        return Clock.systemUTC()
    }

    @Bean
    fun categoriesService(repository: CategoriesRepository): CategoriesApi {
        return CategoriesApi.create(repository)
    }

    @Bean
    fun plansQueries(repository: PlansRepositoryJdbcAdapter): PlansQueries {
        return repository
    }

    @Bean
    fun categoriesRepository(jdbc: JdbcOperations, transactions: TransactionOperations): CategoriesRepository {
        return CategoriesRepositoryJdbcAdapter(jdbc, transactions)
    }

    @Bean
    fun plansRepositoryJdbcAdapter(clock: Clock, jdbc: JdbcOperations, transactions: TransactionOperations): PlansRepositoryJdbcAdapter {
        return PlansRepositoryJdbcAdapter(clock, jdbc)
    }

    @Bean
    fun jdbc(dataSource: DataSource): JdbcOperations {
        return JdbcTemplate(dataSource)
    }

    @Bean
    fun transactions(transactionManager: PlatformTransactionManager): TransactionOperations {
        return TransactionTemplate(transactionManager)
    }
}