package com.smtm.application.spring.resources

import arrow.core.getOrElse
import com.smtm.api.LinkFactory
import com.smtm.api.MediaType
import com.smtm.core.api.PlansApi
import com.smtm.core.domain.NumericId
import com.smtm.core.domain.OwnerId
import com.smtm.core.domain.plans.PlansProblem
import com.smtm.application.spring.conversions.Plans.toDto
import com.smtm.application.spring.conversions.Plans.toPlanDefinition
import com.smtm.application.spring.conversions.Plans.toPlannedCategories
import com.smtm.application.spring.conversions.Violations.toDto
import com.smtm.api.v1.ApiProblemDto
import com.smtm.api.v1.PlanDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(PlansResource.PATH)
class PlansResource(
    private val plansApi: PlansApi,
    private val ownerIdProvider: () -> OwnerId,
    private val linksFactory: LinkFactory
) {

    @GetMapping(
        value = ["/{id}"],
        produces = [MediaType.VERSION_1_JSON]
    )
    fun find(@PathVariable("id") id: Long): ResponseEntity<*> =
        plansApi
            .find(NumericId.of(id))
            .map { it.toDto(linksFactory) }
            .map { ResponseEntity.ok(it) }
            .getOrElse(PlansProblemHandler::handle)

    @PostMapping(
        consumes = [MediaType.VERSION_1_JSON],
        produces = [MediaType.VERSION_1_JSON]
    )
    fun create(@RequestBody plan: PlanDto): ResponseEntity<*> =
        plansApi
            .save(plan.toPlanDefinition(), plan.toPlannedCategories(), ownerIdProvider())
            .map { it.toDto(linksFactory) }
            .map { ResponseEntity.status(201).body(it) }
            .getOrElse(PlansProblemHandler::handle)

    @PutMapping(
        path = ["/{id}"],
        consumes = [MediaType.VERSION_1_JSON],
        produces = [MediaType.VERSION_1_JSON]
    )
    fun update(@RequestBody plan: PlanDto): ResponseEntity<*> =
        plansApi
            .save(plan.toPlanDefinition(), plan.toPlannedCategories(), ownerIdProvider())
            .map { it.toDto(linksFactory) }
            .map { ResponseEntity.ok(it) }
            .getOrElse(PlansProblemHandler::handle)

    companion object {

        const val PATH = "/plans"
    }
}

private object PlansProblemHandler {

    fun handle(problem: PlansProblem): ResponseEntity<*> =
        when (problem) {
            is PlansProblem.Violations -> problem.violations
                .map { it.toDto() }
                .let { ApiProblemDto.ConstraintViolations(it) }
                .let { ResponseEntity.status(422).body(it) }

            is PlansProblem.UnknownPlan -> ApiProblemDto.UnknownResource()
                .let { ResponseEntity.status(404).body(it) }

            is PlansProblem.RepositoryProblem -> ApiProblemDto.Undefined()
                .let { ResponseEntity.status(500).body(it) }
        }
}
