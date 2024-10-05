package com.smtm.infrastructure.persistence.categories

import com.smtm.core.domain.EntityId
import com.smtm.core.domain.categories.Category
import com.smtm.core.domain.tags.Tags
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

    fun toDomain(): Tags<Category> =
        Tags(
            id = EntityId.of(ownerId),
            version = DomainEntityVersion.of(version),
            tagsCollection = categories.map { it.toDomain() }
        )

    companion object {

        fun from(categories: Tags<Category>): CategorySetEntity {
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
