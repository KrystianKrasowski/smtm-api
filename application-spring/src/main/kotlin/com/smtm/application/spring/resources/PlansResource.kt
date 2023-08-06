package com.smtm.application.spring.resources

import arrow.core.getOrElse
import com.smtm.application.LinkFactory
import com.smtm.application.MediaType
import com.smtm.application.api.PlansApi
import com.smtm.application.domain.NumericId
import com.smtm.application.domain.OwnerId
import com.smtm.application.domain.categories.Category
import com.smtm.application.domain.plans.NewPlan
import com.smtm.application.domain.plans.Plan
import com.smtm.application.domain.plans.PlanDefinition
import com.smtm.application.domain.plans.PlannedCategory
import com.smtm.application.domain.plans.PlansProblem
import com.smtm.application.v1.ApiProblemDto
import com.smtm.application.v1.CategoryDto
import com.smtm.application.v1.MoneyDto
import com.smtm.application.v1.NewPlanDto
import com.smtm.application.v1.PeriodDto
import com.smtm.application.v1.PlanDto
import org.javamoney.moneta.Money
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

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
            .map { it.toResponse() }
            .getOrElse(PlansProblemHandler::handle)

    @PostMapping(
        consumes = [MediaType.VERSION_1_JSON],
        produces = [MediaType.VERSION_1_JSON]
    )
    fun create(@RequestBody newPlanDto: NewPlanDto): ResponseEntity<*> =
        plansApi
            .create(newPlanDto.toNewPlan(), ownerIdProvider())
            .map { it.toResponse() }
            .getOrElse(PlansProblemHandler::handle)

    private fun NewPlanDto.toNewPlan() =
        NewPlan(
            definition = PlanDefinition(
                id = NumericId.UNSETTLED,
                name = name,
                period = period.start..period.end
            ),
            entries = entries.map { it.toNewPlanEntry() }
        )

    private fun NewPlanDto.Entry.toNewPlanEntry() =
        NewPlan.Entry(
            categoryId = NumericId.of(categoryId),
            value = Money.of(value.amount, value.currencyCode)
        )

    private fun Plan.toResponse(): ResponseEntity<PlanDto> =
        toDto()
            .let { ResponseEntity.status(201).body(it) }

    private fun Plan.toDto() =
        PlanDto(
            links = mapOf(
                "self" to linksFactory.create("$PATH/${id.value}"),
            ),
            id = id.value,
            name = name,
            period = PeriodDto(
                start = start,
                end = end
            ),
            entries = entries.map { it.toDto() }
        )

    private fun PlannedCategory.toDto() =
        PlanDto.Entry(
            category = category.toDto(),
            value = MoneyDto(
                amount = value.number.numberValue(BigDecimal::class.java),
                currencyCode = value.currency.currencyCode
            )
        )

    private fun Category.toDto(): CategoryDto =
        DtoFactory(linksFactory).create(this)

    companion object {

        const val PATH = "/plans"
    }
}

private object PlansProblemHandler {

    fun handle(problem: PlansProblem): ResponseEntity<*> =
        when (problem) {
            is PlansProblem.Violations -> problem.violations
                .map { DtoFactory.create(it) }
                .let { ApiProblemDto.ConstraintViolations(it) }
                .let { ResponseEntity.status(422).body(it) }

            is PlansProblem.UnknownPlan -> ApiProblemDto.UnknownResource()
                .let { ResponseEntity.status(404).body(it) }

            is PlansProblem.RepositoryProblem -> ApiProblemDto.Undefined()
                .let { ResponseEntity.status(500).body(it) }
        }
}
