package com.smtm.infrastructure.persistence.plans

import com.smtm.core.domain.EntityId
import com.smtm.core.domain.plans.Plan
import com.smtm.infrastructure.persistence.toCents
import com.smtm.infrastructure.persistence.toMonetaryAmount
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "plan_entries")
@IdClass(PlanEntryId::class)
internal open class PlanEntryEntity(

    @Id
    @ManyToOne
    @JoinColumn(name = "plan_id")
    open val plan: PlanEntity,

    @Id
    @JoinColumn(name = "category_id")
    open val categoryId: String,

    @Column(name = "amount")
    open val amount: Int,

    @Column(name = "currency")
    open val currency: String,
) {

    fun toPlanEntry(): Plan.Entry =
        Plan.Entry(
            categoryId = EntityId.of(categoryId),
            value = amount.toMonetaryAmount(currency)
        )

    companion object {

        fun from(entry: Plan.Entry, planEntity: PlanEntity): PlanEntryEntity =
            PlanEntryEntity(
                plan = planEntity,
                categoryId = entry.categoryId.asString(),
                amount = entry.value.toCents(),
                currency = entry.value.currency.currencyCode
            )
    }
}
