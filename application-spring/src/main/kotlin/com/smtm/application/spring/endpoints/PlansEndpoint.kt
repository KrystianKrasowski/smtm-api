package com.smtm.application.spring.endpoints

import arrow.core.getOrElse
import com.smtm.api.LinkFactory
import com.smtm.api.MediaType
import com.smtm.api.ResourcePaths
import com.smtm.api.v1.ApiProblemDto
import com.smtm.api.v1.PlanDto
import com.smtm.api.v1.PlanResource
import com.smtm.application.spring.conversions.Plans.toEntries
import com.smtm.application.spring.conversions.Plans.toHalResource
import com.smtm.application.spring.conversions.Plans.toHeader
import com.smtm.application.spring.endpoints.exceptions.PlansProblemException
import com.smtm.core.api.PlansApi
import com.smtm.core.api.PlansQueries
import com.smtm.core.domain.EntityId
import com.smtm.core.domain.plans.PlansProblem
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(ResourcePaths.PLANS)
class PlansEndpoint(
    private val plansQueries: PlansQueries,
    private val plansApi: PlansApi,
    private val linksFactory: LinkFactory
) {

    @GetMapping(
        path = ["/{id}"],
        consumes = [MediaType.VERSION_1_JSON],
        produces = [MediaType.VERSION_1_JSON]
    )
    fun getPlan(@PathVariable("id") id: String): ResponseEntity<PlanResource> =
        plansQueries.getPlan(EntityId.of(id))
            .map { it.toHalResource(linksFactory) }
            .map { ResponseEntity.ok(it) }
            .getOrElse { throw PlansProblemException(it) }

    @PostMapping(
        consumes = [MediaType.VERSION_1_JSON],
        produces = [MediaType.VERSION_1_JSON]
    )
    fun createPlan(@RequestBody planDto: PlanDto): ResponseEntity<PlanResource> =
        plansApi.create(planDto.toHeader(), planDto.toEntries())
            .map { it.toHalResource(linksFactory) }
            .map { ResponseEntity.created(it.getSelfURI()).body(it) }
            .getOrElse { throw PlansProblemException(it) }

    @PutMapping(
        path = ["/{id}"],
        consumes = [MediaType.VERSION_1_JSON],
        produces = [MediaType.VERSION_1_JSON]
    )
    fun updatePlan(
        @PathVariable("id") id: String,
        @RequestBody planDto: PlanDto
    ): ResponseEntity<Nothing> =
        plansApi.update(planDto.toHeader(id), planDto.toEntries())
            .map { ResponseEntity.noContent().build<Nothing>() }
            .getOrElse { throw PlansProblemException(it) }

    @DeleteMapping("/{id}")
    fun deletePlan(@PathVariable("id") id: String): ResponseEntity<Nothing> =
        EntityId.of(id)
            .let { PlansProblem.notFound(it) }
            .let { throw PlansProblemException(it) }

    @ExceptionHandler
    fun handleCategoriesProblemException(exception: PlansProblemException): ResponseEntity<ApiProblemDto> =
        exception.toResponseEntity()
}
