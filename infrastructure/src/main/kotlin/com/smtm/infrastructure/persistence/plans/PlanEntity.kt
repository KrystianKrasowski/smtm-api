package com.smtm.infrastructure.persistence.plans

import com.smtm.core.domain.EntityId
import com.smtm.core.domain.plans.Plan
import com.smtm.core.domain.plans.PlanHeader
import com.smtm.infrastructure.persistence.categories.CategoryEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import jakarta.persistence.Version
import java.time.LocalDate

@Entity
@Table(name = "plans")
internal open class PlanEntity(

    @Id
    @Column(name = "id")
    open val id: String,

    @Column(name = "owner_id")
    open val ownerId: String,

    @Version
    @Column(name = "version")
    open val version: Int,

    @Column(name = "name")
    open val name: String,

    @Column(name = "start")
    open val start: LocalDate,

    @Column(name = "end")
    open val end: LocalDate,

    @OneToMany(mappedBy = "plan", cascade = [CascadeType.ALL], orphanRemoval = true)
    open val entries: List<PlanEntryEntity>
) {

    fun toPlanHeader(): PlanHeader =
        PlanHeader(
            id = EntityId.of(id),
            name = name,
            period = start..end
        )

    fun toPlan(categories: List<CategoryEntity>): Plan =
        Plan(
            entries = entries.map { it.toPlanEntry() },
            header = toPlanHeader(),
            categories = categories.map { it.toDomain() }
        )
}
