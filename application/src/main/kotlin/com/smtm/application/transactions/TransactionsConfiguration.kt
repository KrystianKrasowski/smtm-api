package com.smtm.application.transactions

import com.smtm.infrastructure.persistence.categories.CategoriesRepositoryAdapter
import com.smtm.infrastructure.persistence.categories.DbCategoriesRepository
import com.smtm.transactions.api.CategoriesRegister
import com.smtm.transactions.categories.categoriesRegisterOf
import com.smtm.transactions.spi.CategoriesRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TransactionsConfiguration {

    @Bean
    fun categoriesRepository(dbRepository: DbCategoriesRepository): CategoriesRepository = CategoriesRepositoryAdapter(dbRepository)

    @Bean
    fun categoriesRegister(repository: CategoriesRepository): CategoriesRegister = categoriesRegisterOf(repository)
}
