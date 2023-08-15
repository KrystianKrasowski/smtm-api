package com.smtm.application.spring.resources

import com.smtm.application.LinkFactory
import com.smtm.application.MediaType
import com.smtm.application.v1.RootDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/")
class RootResource(private val linkFactory: LinkFactory) {

    @GetMapping(
        produces = [MediaType.VERSION_1_JSON]
    )
    fun getRoot(): RootDto {
        return RootDto(
            links = mapOf(
                "self" to linkFactory.create("/"),
                "categories" to linkFactory.create(CategoriesResource.PATH),
                "current-plan-definitions" to linkFactory.create(PlanDefinitionsResource.CURRENT_PLANS_PATH),
                "upcoming-plan-definitions" to linkFactory.create(PlanDefinitionsResource.UPCOMING_PLANS_PATH),
                "archived-plan-definitions" to linkFactory.create(PlanDefinitionsResource.ARCHIVED_PLANS_PATH),
                "plans" to linkFactory.create(PlansResource.PATH)
            )
        )
    }
}
