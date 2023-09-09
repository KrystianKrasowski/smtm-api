package com.smtm.infrastructure.persistence

import com.smtm.application.domain.Icon
import com.smtm.application.domain.NumericId
import com.smtm.application.domain.categories.Categories
import com.smtm.application.domain.categories.CategoriesProblem
import com.smtm.application.domain.categories.Category
import com.smtm.application.domain.ownerIdOf
import com.smtm.application.domain.versionOf
import javax.sql.DataSource
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class CategoriesRepositoryJdbcAdapterTest {

    private lateinit var dataSource: DataSource
    private lateinit var adapter: CategoriesRepositoryJdbcAdapter

    private val initialSqlQueries = listOf(
        "INSERT INTO CATEGORY_SETS (OWNER_ID, VERSION) VALUES (1, 1)",
        "INSERT INTO CATEGORIES (NAME, ICON, OWNER_ID) VALUES ('Rent', 'HOUSE', 1)",
        "INSERT INTO CATEGORIES (NAME, ICON, OWNER_ID) VALUES ('Savings', 'PIGGY_BANK', 1)",
        "INSERT INTO CATEGORIES (NAME, ICON, OWNER_ID) VALUES ('Services', 'LIGHTENING', 1)",
    )

    private val categoriesPrototype = Categories.fetched(
        id = ownerIdOf(1),
        version = versionOf(2),
        list = listOf(
            Category.of(
                id = 1,
                name = "Rent",
                icon = Icon.HOUSE
            ),
            Category.of(
                id = 2,
                name = "Savings",
                icon = Icon.PIGGY_BANK
            ),
            Category.of(
                id = 3,
                name = "Services",
                icon = Icon.LIGHTENING
            )
        )
    )

    @BeforeEach
    fun setUp() {
        dataSource = TestDatabase.setup()
        adapter = CategoriesRepositoryJdbcAdapter(dataSource)
        initialSqlQueries.forEach { dataSource.runSql(it) }
    }

    @Test
    fun `should get categories`() {
        // when
        val categories = adapter.getCategories(ownerIdOf(1)).getOrNull()

        // then
        assertThat(categories?.current).containsOnly(
            Category.of(
                1, name = "Rent",
                icon = Icon.HOUSE
            ),
            Category.of(
                2, name = "Savings",
                icon = Icon.PIGGY_BANK
            ),
            Category.of(
                3, name = "Services",
                icon = Icon.LIGHTENING
            )
        )
    }

    @Test
    fun `should get empty, non-first version categories`() {
        // given
        dataSource.runSql("DELETE FROM CATEGORIES")
        dataSource.runSql("UPDATE CATEGORY_SETS SET VERSION = 2 WHERE OWNER_ID = 1")

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
        val result = Category.newOf(name = "Groceries", icon = Icon.SHOPPING_CART)
            .let { categoriesPrototype.copy(toSave = listOf(it)) }
            .let { adapter.save(it) }
            .getOrNull()


        // then
        assertThat(result?.getByName("Groceries")?.id?.value).isEqualTo(4)
        assertThat(result?.getByName("Groceries")?.icon).isEqualTo(Icon.SHOPPING_CART)
    }

    @ParameterizedTest
    @CsvSource(
        "1, Awesome rent, HOUSE",
        "1, Rent, LIGHTENING"
    )
    fun `should update category`(id: Long, name: String, icon: Icon) {
        // when
        val result = Category.of(NumericId.of(id), name, icon)
            .let { categoriesPrototype.copy(toSave = listOf(it)) }
            .let { adapter.save(it) }
            .getOrNull()

        // then
        assertThat(result?.getById(NumericId.of(id))?.name).isEqualTo(name)
        assertThat(result?.getById(NumericId.of(id))?.icon).isEqualTo(icon)
    }

    @Test
    fun `should violate unique constraint of the name property`() {
        // when
        val problem = Category.newOf(name = "Rent", icon = Icon.SHOPPING_CART)
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
        val categories = Category.of(
            1, name = "Rent",
            icon = Icon.HOUSE
        )
            .let { categoriesPrototype.copy(toDelete = listOf(it)) }
            .let { adapter.save(it) }
            .getOrNull()

        // then
        assertThat(categories?.current).containsExactlyInAnyOrder(
            Category.of(
                2, name = "Savings",
                icon = Icon.PIGGY_BANK
            ),
            Category.of(
                3, name = "Services",
                icon = Icon.LIGHTENING
            )
        )
    }
}
