package com.smtm.infrastructure.persistence.categories

import com.smtm.core.domain.OwnerId
import com.smtm.core.domain.categories.Categories
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import jakarta.persistence.Version
import com.smtm.core.domain.Version.Companion as DomainEntityVersion

@Entity
@Table(name = "category_sets")
internal open class CategorySetEntity(

    @Id
    @Column(name = "owner_id")
    open val ownerId: String,

    @Version
    @Column(name = "version")
    open val version: Int,

    @OneToMany(mappedBy = "categorySet", cascade = [CascadeType.ALL], orphanRemoval = true)
    open val categories: MutableList<CategoryEntity>
) {

    fun toDomain(): Categories =
        Categories(
            id = OwnerId.of(ownerId),
            version = DomainEntityVersion.of(version),
            categoryList = categories.map { it.toDomain() }
        )

    companion object {

        fun from(categories: Categories): CategorySetEntity {
            val categorySetEntity = CategorySetEntity(
                ownerId = categories.id.asString(),
                version = categories.version.asInt(),
                categories = mutableListOf()
            )
            val categoryEntities = categories.map { CategoryEntity.from(it, categorySetEntity) }
            categorySetEntity.categories.addAll(categoryEntities)
            return categorySetEntity
        }
    }
}
