package com.smtm.infrastructure.persistence.plans

import com.smtm.core.domain.EntityId
import com.smtm.core.domain.OwnerId
import com.smtm.core.domain.categories.Category
import com.smtm.core.domain.plans.Plan
import com.smtm.core.domain.plans.PlanHeader
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import jakarta.persistence.Version
import java.time.LocalDate
import com.smtm.core.domain.Version.Companion as DomainVersion

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

    @Column(name = "`end`")
    open val end: LocalDate,

    @OneToMany(mappedBy = "plan", cascade = [CascadeType.ALL], orphanRemoval = true)
    open val entries: MutableList<PlanEntryEntity>
) {

    fun toPlanHeader(): PlanHeader =
        PlanHeader(
            id = EntityId.of(id),
            name = name,
            period = start..end
        )

    fun toPlan(categories: List<Category>): Plan =
        Plan(
            entries = entries.map { it.toPlanEntry() },
            categories = categories,
            version = DomainVersion.of(version),
            header = toPlanHeader(),
        )

    companion object {

        fun from(plan: Plan, ownerId: OwnerId): PlanEntity {
            val planEntity = PlanEntity(
                id = plan.id.asString(),
                ownerId = ownerId.asString(),
                version = plan.version.asInt(),
                name = plan.name,
                start = plan.start,
                end = plan.end,
                entries = mutableListOf()
            )
            val entries = plan.entries.map { PlanEntryEntity.from(it, planEntity) }
            planEntity.entries.addAll(entries)
            return planEntity
        }
    }
}
