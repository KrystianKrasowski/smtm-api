package com.smtm.application.transactions.categories.v1

import com.smtm.application.common.dto.toResponse400
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path = ["/categories"])
class CategoriesController(private val categoriesRegister: CategoriesRegister) {

    @GetMapping(path = ["/{id}"])
    fun get(@PathVariable id: Long): ResponseEntity<*> {
        TODO("Not implemented yet")
    }

    @PostMapping(
        consumes = [CategoryDto.MediaTypeValue],
        produces = [CategoryDto.MediaTypeValue]
    )
    fun create(@RequestBody category: CategoryDto): ResponseEntity<*> = category.toCategory()
        .let { categoriesRegister.accept(it) }
        .toResponse()
}

private fun AcceptingCategory.toResponse() = when (this) {
    is AcceptingCategory.Accepted -> category.toResponse201()
    is AcceptingCategory.Declined -> violations.toResponse400("Provided category violates some of the constraints")
}

private fun Category.toResponse201() = toDto().toResponse201()
