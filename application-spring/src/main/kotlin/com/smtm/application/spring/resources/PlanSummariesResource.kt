package com.smtm.application.spring.resources

import arrow.core.getOrElse
import com.smtm.application.HalCollection
import com.smtm.application.LinkFactory
import com.smtm.application.MediaType
import com.smtm.application.api.PlanSummariesApi
import com.smtm.application.domain.OwnerId
import com.smtm.application.domain.plans.PlanStatus
import com.smtm.application.domain.plans.PlanSummary
import com.smtm.application.domain.plans.PlanSummaries
import com.smtm.application.domain.plans.PlanSummariesProblem
import com.smtm.application.v1.ApiProblemDto
import com.smtm.application.v1.PeriodDto
import com.smtm.application.v1.PlanStatusDto
import com.smtm.application.v1.PlanSummaryDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(PlanSummariesResource.PATH)
class PlanSummariesResource(
    private val planSummaryListApi: PlanSummariesApi,
    private val linkFactory: LinkFactory,
    private val ownerIdProvider: () -> OwnerId
) {

    private val collectionLinks = mapOf(
        "self" to linkFactory.create(PATH)
    )

    @GetMapping(
        produces = [MediaType.VERSION_1_JSON]
    )
    fun getAllPlans(): ResponseEntity<*> {
        return ownerIdProvider()
            .let { planSummaryListApi.getAllPlans(it) }
            .map { it.toResponse() }
            .getOrElse { it.toProblem() }
    }

    private fun PlanStatusDto.toDomain() = when (this) {
        PlanStatusDto.ACTIVE -> PlanStatus.ACTIVE
        PlanStatusDto.FUTURE -> PlanStatus.FUTURE
        PlanStatusDto.PAST -> PlanStatus.PAST
    }

    private fun PlanStatus.toDto() = when (this) {
        PlanStatus.ACTIVE -> PlanStatusDto.ACTIVE
        PlanStatus.FUTURE -> PlanStatusDto.FUTURE
        PlanStatus.PAST -> PlanStatusDto.PAST
    }

    private fun PlanSummaries.toResponse() = toDto()
        .let { ResponseEntity.ok(it) }

    private fun PlanSummariesProblem.toProblem() = ApiProblemDto.Undefined()
        .let { ResponseEntity.status(500).body(it) }

    private fun PlanSummary.toDto() = PlanSummaryDto(
        links = mapOf(
            "self" to linkFactory.create("$PATH/$id")
        ),
        id = id,
        name = name,
        period = PeriodDto(
            start = period.start,
            end = period.endInclusive
        ),
        status = status.toDto()
    )

    private fun PlanSummaries.toDto() = current
        .map { it.toDto() }
        .let { HalCollection(collectionLinks, it.size, it.size, mapOf("plans" to it)) }

    companion object {

        const val PATH = "/plans"
    }
}