package com.smtm.infrastructure.persistence.plans

import com.smtm.application.domain.NumericId
import com.smtm.application.domain.OwnerId
import com.smtm.application.domain.Version
import com.smtm.application.domain.ownerIdOf
import com.smtm.application.domain.plans.Plan
import com.smtm.application.domain.plans.PlanDefinition
import com.smtm.application.domain.versionOf
import java.sql.ResultSet
import java.time.LocalDateTime

internal data class PlanEntity(
    val ownerId: OwnerId,
    val version: Version,
    val id: Long?,
    val name: String?,
    val start: LocalDateTime?,
    val end: LocalDateTime?
) {

    fun toPlanDefinition() = PlanDefinition.existing(
        id = NumericId.of(id),
        name = name!!,
        start = start!!,
        end = end!!
    )

    companion object {

        val MAPPER = { rs: ResultSet, _: Int ->
            PlanEntity(
                ownerId = ownerIdOf(rs.getLong("owner_id")),
                version = versionOf(rs.getInt("version")),
                id = rs.getLong("id"),
                name = rs.getString("name"),
                start = rs.getTimestamp("start").toLocalDateTime(),
                end = rs.getTimestamp("end").toLocalDateTime()
            )
        }

        fun of(plan: Plan) = PlanEntity(
            ownerId = plan.ownerId,
            version = plan.version,
            id = plan.id.takeUnless { it == NumericId.UNSETTLED }?.value,
            name = plan.name,
            start = plan.start,
            end = plan.end
        )
    }
}
