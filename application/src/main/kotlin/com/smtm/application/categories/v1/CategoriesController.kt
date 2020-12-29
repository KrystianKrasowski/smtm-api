package com.smtm.application.categories.v1

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path = ["/categories"])
class CategoriesController {

    @GetMapping(path = ["/{id}"])
    fun get(@PathVariable id: Long): ResponseEntity<*> {
        TODO("Not implemented yet")
    }

    @PostMapping(
            consumes = [CategoryDto.MediaTypeValue],
            produces = [CategoryDto.MediaTypeValue]
    )
    fun create(@RequestBody category: CategoryDto): ResponseEntity<*> = categoryOf(1, category.name, category.icon)
            .toResponse201()
}

private fun Category.toResponse201() = categoryDtoOf(this).toResponse201()
