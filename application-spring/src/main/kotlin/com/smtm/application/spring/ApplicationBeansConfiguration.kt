package com.smtm.application.spring

import com.smtm.application.LinkFactory
import com.smtm.application.api.CategoriesApi
import com.smtm.application.domain.OwnerId
import com.smtm.application.domain.ownerIdOf
import com.smtm.application.spi.CategoriesRepository
import com.smtm.application.spring.infrastructure.persistence.CategoriesRepositoryJdbcAdapter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.TransactionOperations
import org.springframework.transaction.support.TransactionTemplate
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
    fun categoriesService(repository: CategoriesRepository): CategoriesApi {
        return CategoriesApi.create(repository)
    }

    @Bean
    fun categoriesRepository(jdbc: JdbcOperations, transactions: TransactionOperations): CategoriesRepository {
        return CategoriesRepositoryJdbcAdapter(jdbc, transactions)
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