package com.smtm.infrastructure.persistence.categories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
internal interface CategorySetsJpaRepository : JpaRepository<CategorySetEntity, String> {

    fun findByOwnerId(ownerId: String): CategorySetEntity?
}
