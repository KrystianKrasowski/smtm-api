package com.smtm.application.spring.resources

import com.smtm.application.LinkFactory
import com.smtm.application.MediaType
import com.smtm.application.v1.CategoryDto
import com.smtm.application.v1.NewPlanDto
import com.smtm.application.v1.PlanDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(PlansResource.PATH)
class PlansResource(
    private val linksFactory: LinkFactory
) {

    @PostMapping(
        consumes = [MediaType.VERSION_1_JSON],
        produces = [MediaType.VERSION_1_JSON]
    )
    fun create(@RequestBody newPlanDto: NewPlanDto): ResponseEntity<*> {
        return newPlanDto.toPlanDto()
            .let { ResponseEntity.status(201).body(it) }
    }

    private fun NewPlanDto.toPlanDto() = PlanDto(
        links = mapOf("self" to linksFactory.create("$PATH/1")),
        id = 1,
        name = name,
        period = period,
        entries = entries.map { it.toEntry() }
    )

    private fun NewPlanDto.Entry.toEntry() = PlanDto.Entry(
        category = CategoryDto(
            links = mapOf("self" to linksFactory.create("${CategoriesResource.PATH}/$categoryId")),
            id = categoryId,
            name = "Some name",
            icon = "FOLDER"
        ),
        value = value
    )

    companion object {

        const val PATH = "/plans"
    }
}