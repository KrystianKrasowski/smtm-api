package com.smtm.application.spring.infrastructure.storage

import com.smtm.application.domain.Icon
import com.smtm.application.domain.categories.Categories
import com.smtm.application.domain.categories.CategoriesProblem
import com.smtm.application.domain.categories.categoryOf
import com.smtm.application.domain.categories.existingCategoryOf
import com.smtm.application.domain.categories.newCategoryOf
import com.smtm.application.domain.ownerIdOf
import com.smtm.application.domain.versionOf
import com.smtm.application.spring.infrastructure.persistence.CategoriesRepositoryJdbcAdapter
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.transaction.support.TransactionOperations

@SpringBootTest
class CategoriesRepositoryJdbcAdapterTest {

    @Autowired
    private lateinit var jdbc: JdbcOperations

    @Autowired
    private lateinit var transactions: TransactionOperations

    private val categoriesPrototype = Categories.fetched(
        id = ownerIdOf(1),
        version = versionOf(2),
        list = listOf(
            existingCategoryOf(1, "Rent", Icon.HOUSE),
            existingCategoryOf(2, "Savings", Icon.PIGGY_BANK),
            existingCategoryOf(3, "Services", Icon.LIGHTENING)
        )
    )

    private val adapter get() = CategoriesRepositoryJdbcAdapter(jdbc, transactions)

    private val initialSqlQueries = listOf(
        "INSERT INTO CATEGORY_SETS (OWNER_ID, VERSION) VALUES (1, 1)",
        "INSERT INTO CATEGORIES (NAME, ICON, OWNER_ID) VALUES ('Rent', 'HOUSE', 1)",
        "INSERT INTO CATEGORIES (NAME, ICON, OWNER_ID) VALUES ('Savings', 'PIGGY_BANK', 1)",
        "INSERT INTO CATEGORIES (NAME, ICON, OWNER_ID) VALUES ('Services', 'LIGHTENING', 1)",
    )

    @BeforeEach
    fun setUp() {
        initialSqlQueries.forEach(jdbc::update)
    }

    @AfterEach
    fun tearDown() {
        jdbc.execute("DELETE FROM CATEGORIES")
        jdbc.execute("DELETE FROM CATEGORY_SETS")
        jdbc.execute("ALTER TABLE CATEGORIES ALTER COLUMN ID RESTART WITH 1")
    }

    @Test
    fun `should get categories`() {
        // when
        val categories = adapter.getCategories(ownerIdOf(1)).getOrNull()

        // then
        assertThat(categories?.current).containsOnly(
            existingCategoryOf(1, "Rent", Icon.HOUSE),
            existingCategoryOf(2, "Savings", Icon.PIGGY_BANK),
            existingCategoryOf(3, "Services", Icon.LIGHTENING)
        )
    }

    @Test
    fun `should get empty, non-first version categories`() {
        // given
        jdbc.execute("DELETE FROM CATEGORIES")
        jdbc.execute("UPDATE CATEGORY_SETS SET VERSION = 2 WHERE OWNER_ID = 1")

        // when
        val result = adapter.getCategories(ownerIdOf(1)).getOrNull()

        // then
        assertThat(result?.version).isEqualTo(versionOf(2))
        assertThat(result?.current).isEmpty()
    }

    @Test
    fun `should get empty categories for new owner`() {
        // when
        val categories = adapter.getCategories(ownerIdOf(99)).getOrNull()

        // then
        assertThat(categories?.id).isEqualTo(ownerIdOf(99))
        assertThat(categories?.current).isEmpty()
        assertThat(categories?.version).isEqualTo(versionOf(0))
    }

    @Test
    fun `should save category`() {
        // when
        val result = newCategoryOf("Groceries", Icon.SHOPPING_CART)
            .let { categoriesPrototype.copy(toSave = listOf(it)) }
            .let { adapter.save(it) }
            .getOrNull()


        // then
        assertThat(result?.getByName("Groceries")?.id).isEqualTo(4)
        assertThat(result?.getByName("Groceries")?.icon).isEqualTo(Icon.SHOPPING_CART)
    }

    @ParameterizedTest
    @CsvSource(
        "1, Awesome rent, HOUSE",
        "1, Rent, LIGHTENING"
    )
    fun `should update category`(id: Long, name: String, icon: Icon) {
        // when
        val result = categoryOf(id, name, icon)
            .let { categoriesPrototype.copy(toSave = listOf(it)) }
            .let { adapter.save(it) }
            .getOrNull()

        // then
        assertThat(result?.getById(id)?.name).isEqualTo(name)
        assertThat(result?.getById(id)?.icon).isEqualTo(icon)
    }

    @Test
    fun `should violate unique constraint of the name property`() {
        // when
        val problem = newCategoryOf("Rent", Icon.SHOPPING_CART)
            .let { categoriesPrototype.copy(toSave = listOf(it)) }
            .let { adapter.save(it) }
            .leftOrNull()

        val version = adapter
            .getCategories(ownerIdOf(1))
            .map { it.version }
            .getOrNull()

        // then
        assertThat(problem).isEqualTo(CategoriesProblem.Other("Cannot save categories"))
        assertThat(version).isEqualTo(versionOf(1))
    }

    @Test
    fun `should delete category`() {
        // when
        val categories = existingCategoryOf(1, "Rent", Icon.HOUSE)
            .let { categoriesPrototype.copy(toDelete = listOf(it)) }
            .let { adapter.save(it) }
            .getOrNull()

        // then
        assertThat(categories?.current).containsExactlyInAnyOrder(
            existingCategoryOf(2, "Savings", Icon.PIGGY_BANK),
            existingCategoryOf(3, "Services", Icon.LIGHTENING)
        )
    }
}