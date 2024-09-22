package com.smtm.infrastructure.persistence.categories

import com.smtm.core.domain.OwnerId
import com.smtm.core.domain.categories.Categories
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
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

    @OneToMany(cascade = [CascadeType.ALL])
    @JoinColumn(name = "owner_id")
    open val categories: List<CategoryEntity>
) {

    fun toDomain(): Categories =
        Categories(
            id = OwnerId.of(ownerId),
            version = DomainEntityVersion.of(version),
            actual = categories.map { it.toDomain() }
        )

    companion object {

        fun from(categories: Categories): CategorySetEntity =
            CategorySetEntity(
                ownerId = categories.id.value,
                version = categories.version.value,
                categories = categories.map { CategoryEntity.from(it, categories.id) }
            )
    }
}
