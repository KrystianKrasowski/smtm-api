package com.smtm.application.spring.endpoints

import arrow.core.getOrElse
import com.smtm.api.LinkFactory
import com.smtm.api.MediaType
import com.smtm.api.ResourcePaths
import com.smtm.api.v1.ApiProblemDto
import com.smtm.application.spring.conversions.CategoriesConversions.toHalCollection
import com.smtm.core.api.CategoriesApi
import com.smtm.core.domain.OwnerId
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(ResourcePaths.CATEGORIES)
class CategoriesEndpoint(
    private val categoriesApi: CategoriesApi,
    private val ownerIdProvider: () -> OwnerId,
    private val linkFactory: LinkFactory
) {

    @GetMapping(
        consumes = [MediaType.VERSION_1_JSON],
        produces = [MediaType.VERSION_1_JSON]
    )
    fun getAll(): ResponseEntity<*> =
        categoriesApi.getAll(ownerIdProvider())
            .map { it.toHalCollection(linkFactory) }
            .map { ResponseEntity.ok(it) }
            .getOrElse {
                ResponseEntity
                    .internalServerError()
                    .body(ApiProblemDto.Undefined())
            }
}
