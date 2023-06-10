package com.smtm.application.spring.infrastructure.storage

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.smtm.application.domain.Icon
import com.smtm.application.domain.OwnerId
import com.smtm.application.domain.categories.Categories
import com.smtm.application.domain.categories.CategoriesProblem
import com.smtm.application.domain.categories.Category
import com.smtm.application.domain.ownerIdOf
import com.smtm.application.domain.versionOf
import com.smtm.application.repository.CategoriesRepository
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

class CategoriesRepositoryAdapter(private val jpaRepository: CategorySetsJpaRepository) : CategoriesRepository {

    override fun getCategories(ownerId: OwnerId): Either<CategoriesProblem, Categories> {
        return jpaRepository
            .runCatching { findByOwnerId(ownerId.value) }
            .map { it?.toDomain() ?: Categories.empty(ownerId) }
            .map { it.right() }
            .getOrElse { it.toCategoriesProblem().left() }
    }

    override fun save(categories: Categories): Either<CategoriesProblem, List<Category>> {
        return CategorySetEntity.fromDomain(categories)
            .runCatching { jpaRepository.saveAndFlush(this) }
            .map { it.toDomain() }
            .map { it.list }
            .map { list ->
                list.filter { categories.newCategoryNames.contains(it.name) }
            }
            .map { it.right() }
            .getOrElse { it.toCategoriesProblem().left() }
    }

    private fun Throwable.toCategoriesProblem() = (message ?: "Something went wrong")
        .let { CategoriesProblem.Other(it) }
}

@Repository
interface CategorySetsJpaRepository: JpaRepository<CategorySetEntity, Long> {

    fun findByOwnerId(ownerId: Long): CategorySetEntity?
}

@Entity
@Table(name = "category_sets")
open class CategorySetEntity {

    @Id
    open var ownerId: Long? = null

    @Column(name = "version")
    open var version: Int? = null

    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinColumn(name = "owner_id")
    open var categories: MutableList<CategoryEntity> = mutableListOf()

    fun toDomain(): Categories {
        return Categories(
            id = ownerIdOf(ownerId!!),
            version = versionOf(version!!.toLong()),
            list = categories.map { it.toDomain() }
        )
    }

    companion object {

        fun fromDomain(categories: Categories): CategorySetEntity {
            val entity = CategorySetEntity()

            entity.ownerId = categories.id.value
            entity.version = categories.version.value.toInt()
            entity.categories = categories.list
                .map { CategoryEntity.fromDomain(it, categories.id.value) }
                .toMutableList()

            return entity
        }
    }
}

@Entity
@Table(name = "categories")
open class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null

    @Column(name = "owner_id")
    open var ownerId: Long? = null

    @Column(name = "name")
    open var name: String? = null

    @Column(name = "icon")
    open var icon: String? = null

    fun toDomain(): Category {
        return Category(
            id = id!!,
            name = name!!,
            icon = Icon.valueOf(icon!!)
        )
    }

    companion object {

        fun fromDomain(category: Category, ownerId: Long): CategoryEntity {
            val entity = CategoryEntity()
            entity.id = category.id
            entity.ownerId = ownerId
            entity.name = category.name
            entity.icon = category.icon.name
            return entity
        }
    }
}