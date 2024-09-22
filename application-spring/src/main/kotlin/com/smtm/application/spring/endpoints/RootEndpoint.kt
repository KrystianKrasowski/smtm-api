package com.smtm.application.spring.endpoints

import com.smtm.api.LinkFactory
import com.smtm.api.MediaType
import com.smtm.api.ResourcePaths
import com.smtm.api.v1.RootDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(ResourcePaths.ROOT)
class RootEndpoint(private val linkFactory: LinkFactory) {

    @GetMapping(
        produces = [MediaType.VERSION_1_JSON]
    )
    fun getRoot(): RootDto {
        return RootDto(
            links = mapOf(
                "self" to linkFactory.create(ResourcePaths.ROOT),
                "categories" to linkFactory.create(ResourcePaths.CATEGORIES),
                "plans" to linkFactory.create(ResourcePaths.PLANS)
            )
        )
    }
}
