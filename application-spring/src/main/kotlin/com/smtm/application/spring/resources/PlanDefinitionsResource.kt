package com.smtm.application.spring.resources

import arrow.core.getOrElse
import com.smtm.application.LinkFactory
import com.smtm.application.MediaType
import com.smtm.application.api.PlansQueries
import com.smtm.application.domain.OwnerId
import com.smtm.application.domain.plans.PlanDefinition
import com.smtm.application.toHalCollection
import com.smtm.application.v1.ApiProblemDto
import com.smtm.application.v1.PeriodDto
import com.smtm.application.v1.PlanDefinitionDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PlanDefinitionsResource(
    private val plansQueries: PlansQueries,
    private val linkFactory: LinkFactory,
    private val ownerIdProvider: () -> OwnerId
) {

    private val undefinedProblem = ApiProblemDto.Undefined()
        .let { ResponseEntity.status(500).body(it) }

    @GetMapping(
        path = [CURRENT_PLANS_PATH],
        produces = [MediaType.VERSION_1_JSON]
    )
    fun getCurrentPlans(): ResponseEntity<*> {
        return ownerIdProvider()
            .let { plansQueries.getCurrentPlans(it) }
            .map { it.toResponse(CURRENT_PLANS_PATH) }
            .getOrElse { undefinedProblem }
    }

    @GetMapping(
        path = [UPCOMING_PLANS_PATH],
        produces = [MediaType.VERSION_1_JSON]
    )
    fun getUpcomingPlans(): ResponseEntity<*> {
        return ownerIdProvider()
            .let { plansQueries.getUpcomingPlans(it) }
            .map { it.toResponse(UPCOMING_PLANS_PATH) }
            .getOrElse { undefinedProblem }
    }

    @GetMapping(
        path = [ARCHIVED_PLANS_PATH],
        produces = [MediaType.VERSION_1_JSON]
    )
    fun getArchivedPlans(): ResponseEntity<*> {
        return ownerIdProvider()
            .let { plansQueries.getArchivedPlans(it) }
            .map { it.toResponse(ARCHIVED_PLANS_PATH) }
            .getOrElse { undefinedProblem }
    }

    private fun List<PlanDefinition>.toResponse(collectionPath: String) = map { it.toDto() }
        .toHalCollection(createCollectionLinks(collectionPath), "plans")
        .let { ResponseEntity.ok(it) }

    private fun PlanDefinition.toDto() = PlanDefinitionDto(
        links = mapOf(
            "self" to linkFactory.create("${PlansResource.PATH}/$id")
        ),
        id = id,
        name = name,
        period = PeriodDto(
            start = period.start,
            end = period.endInclusive
        )
    )

    private fun createCollectionLinks(path: String) = mapOf(
        "self" to linkFactory.create(path)
    )

    companion object {

        const val CURRENT_PLANS_PATH = "/plan-definitions/current"
        const val UPCOMING_PLANS_PATH = "/plan-definitions/upcoming"
        const val ARCHIVED_PLANS_PATH = "/plan-definitions/archived"
    }
}