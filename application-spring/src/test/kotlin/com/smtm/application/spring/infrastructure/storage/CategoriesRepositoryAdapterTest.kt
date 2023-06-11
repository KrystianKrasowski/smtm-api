package com.smtm.application.spring.infrastructure.storage

import com.smtm.application.domain.Icon
import com.smtm.application.domain.categories.Categories
import com.smtm.application.domain.categories.categoryOf
import com.smtm.application.domain.categories.newCategoryOf
import com.smtm.application.domain.ownerIdOf
import com.smtm.application.domain.versionOf
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@DataJpaTest
class CategoriesRepositoryAdapterTest {

    @Autowired
    private lateinit var jpaRepository: CategorySetsJpaRepository

    private val adapter get() = CategoriesRepositoryAdapter(jpaRepository)

    @BeforeEach
    fun setUp() {
        val set = emptyCategorySetOf(ownerId = 1, version = 1)
        set.categories = mutableListOf(
            categoryEntityOf(id = 1, name = "Rent", icon = Icon.HOUSE, set),
            categoryEntityOf(id = 2, name = "Savings", icon = Icon.PIGGY_BANK, set),
            categoryEntityOf(id = 3, name = "Services", icon = Icon.LIGHTENING, set),
        )
        jpaRepository.saveAndFlush(set)
    }

    @AfterEach
    fun tearDown() {
        jpaRepository.deleteAll()
    }

    @Test
    fun `should get categories`() {
        // when
        val categories = adapter.getCategories(ownerIdOf(1)).getOrNull()

        // then
        assertThat(categories?.list).containsOnly(
            categoryOf(1, "Rent", Icon.HOUSE),
            categoryOf(2, "Savings", Icon.PIGGY_BANK),
            categoryOf(3, "Services", Icon.LIGHTENING)
        )
    }

    @Test
    fun `should save category`() {
        // when
        val categories = Categories(
            id = ownerIdOf(1), version = versionOf(2), list = listOf(
                categoryOf(1, "Rent", Icon.HOUSE),
                categoryOf(2, "Savings", Icon.PIGGY_BANK),
                categoryOf(3, "Services", Icon.LIGHTENING),
                newCategoryOf("Groceries", Icon.SHOPPING_CART)
            )
        )

        val categoryList = adapter.save(categories).getOrNull()

        // then
        assertThat(categoryList).anyMatch { it.name == "Groceries" && it.icon == Icon.SHOPPING_CART }
    }
}

private fun emptyCategorySetOf(ownerId: Long, version: Int) = CategorySetEntity()
    .apply {
        this.ownerId = ownerId
        this.version = version
        this.categories = mutableListOf()
    }

private fun categoryEntityOf(id: Long? = null, name: String, icon: Icon, set: CategorySetEntity) = CategoryEntity()
    .apply {
        this.id = id
        this.name = name
        this.icon = icon.name
        this.set = set
    }