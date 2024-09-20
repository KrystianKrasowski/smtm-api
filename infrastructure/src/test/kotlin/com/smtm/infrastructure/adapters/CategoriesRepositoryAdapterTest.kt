package com.smtm.infrastructure.adapters

import arrow.core.getOrElse
import assertk.assertThat
import assertk.assertions.containsExactlyInAnyOrder
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import com.smtm.core.domain.EntityId
import com.smtm.core.domain.Icon
import com.smtm.core.domain.OwnerId
import com.smtm.core.domain.Version
import com.smtm.core.domain.categories.Category
import com.smtm.infrastructure.persistence.TestDatabase
import com.smtm.infrastructure.persistence.runSql
import javax.sql.DataSource
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CategoriesRepositoryAdapterTest {

    private val dataSource: DataSource = TestDatabase.setup()
    private val adapter get() = CategoriesRepositoryAdapter(dataSource)

    @BeforeEach
    fun setUp() {
        dataSource.runSql("INSERT INTO category_sets (owner_id, version) VALUES ('owner-1', 23)")
        dataSource.runSql(
            """
            INSERT INTO categories (id, owner_id, name, icon)
            VALUES
            ('category-1', 'owner-1', 'Rent', 'HOUSE'),
            ('category-2', 'owner-1', 'Savings', 'PIGGY_BANK'),
            ('category-3', 'owner-1', 'Groceries', 'SHOPPING_CART')
        """.trimIndent()
        )
    }

    @AfterEach
    fun tearDown() {
        dataSource.runSql("DELETE FROM plans CASCADE")
        dataSource.runSql("DELETE FROM category_sets CASCADE")
    }

    @Test
    fun `should return categories for owner`() {
        // when
        val categories = adapter.getCategories(OwnerId.of("owner-1"))
            .getOrElse { error("Should return categories in this case") }

        // then
        assertThat(categories.id).isEqualTo(OwnerId.of("owner-1"))
        assertThat(categories.version).isEqualTo(Version.of(23))
        assertThat(categories).containsExactlyInAnyOrder(
            categoryOf("category-1", "Rent", "HOUSE"),
            categoryOf("category-2", "Savings", "PIGGY_BANK"),
            categoryOf("category-3", "Groceries", "SHOPPING_CART"),
        )
    }

    @Test
    fun `should return empty categories`() {
        // when
        val categories = adapter.getCategories(OwnerId.of("owner-99"))
            .getOrElse { error("Should return categories in this case") }

        // then
        assertThat(categories.id).isEqualTo(OwnerId.of("owner-99"))
        assertThat(categories.version).isEqualTo(Version.of(0))
        assertThat(categories).isEmpty()
    }
}

private fun categoryOf(id: String, name: String, icon: String): Category =
    Category.of(EntityId.of(id), name, Icon.valueOf(icon))
