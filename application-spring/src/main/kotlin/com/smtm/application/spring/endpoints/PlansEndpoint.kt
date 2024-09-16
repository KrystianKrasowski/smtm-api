package com.smtm.application.spring.endpoints

import arrow.core.getOrElse
import com.smtm.api.LinkFactory
import com.smtm.api.MediaType
import com.smtm.api.v1.ApiProblemDto
import com.smtm.api.v1.PlanDto
import com.smtm.application.spring.conversions.Plans.toHalResource
import com.smtm.core.api.PlansQueries
import com.smtm.core.domain.NumericId
import com.smtm.core.domain.OwnerId
import com.smtm.core.domain.plans.PlansProblem
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(PlansEndpoint.PATH)
class PlansEndpoint(
    private val plansQueries: PlansQueries,
    private val ownerIdProvider: () -> OwnerId,
    private val linksFactory: LinkFactory
) {

    @GetMapping(
        path = ["/{id}"],
        consumes = [MediaType.VERSION_1_JSON],
        produces = [MediaType.VERSION_1_JSON]
    )
    fun getPlan(@PathVariable("id") id: Long): ResponseEntity<*> =
        plansQueries.getPlan(NumericId.of(id), ownerIdProvider())
            .map { it.toHalResource(linksFactory) }
            .map { ResponseEntity.ok(it) }
            .getOrElse { it.toResponseEntity() }

    @PostMapping(
        consumes = [MediaType.VERSION_1_JSON],
        produces = [MediaType.VERSION_1_JSON]
    )
    fun createPlan(@RequestBody planDto: PlanDto): ResponseEntity<*> =
        ResponseEntity
            .status(201)
            .header("Location", "http://localhost:8080/plans/2")
            .build<Nothing>()

    companion object {

        const val PATH = "/plans"
    }
}

private fun PlansProblem.toResponseEntity(): ResponseEntity<ApiProblemDto> =
    when (this) {
        is PlansProblem.Failure -> ResponseEntity
            .internalServerError()
            .body(ApiProblemDto.Undefined())

        is PlansProblem.NotFound -> ResponseEntity
            .status(404)
            .header("Content-Type", MediaType.PROBLEM)
            .body(ApiProblemDto.UnknownResource())
    }
