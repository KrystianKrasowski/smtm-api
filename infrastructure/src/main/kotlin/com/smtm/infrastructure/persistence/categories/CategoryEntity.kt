package com.smtm.infrastructure.persistence.categories

import com.smtm.core.domain.EntityId
import com.smtm.core.domain.Icon
import com.smtm.core.domain.OwnerId
import com.smtm.core.domain.categories.Category
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "categories")
internal open class CategoryEntity(

    @Id
    @Column(name = "id")
    open val id: String,

    @Column(name = "owner_id")
    open val ownerId: String,

    @Column(name = "name", unique = true)
    open val name: String,

    @Column(name = "icon")
    open val icon: String,
) {

    fun toDomain(): Category =
        Category(
            id = EntityId.of(id),
            name = name,
            icon = Icon.valueOfOrDefault(icon)
        )

    companion object {

        fun from(category: Category, ownerId: OwnerId): CategoryEntity =
            CategoryEntity(
                id = category.id.toString(),
                ownerId = ownerId.value,
                name = category.name,
                icon = category.icon.name
            )
    }
}
