package com.smtm.application.spring.endpoints

import arrow.core.getOrElse
import com.smtm.api.HalCollection
import com.smtm.api.LinkFactory
import com.smtm.api.MediaType
import com.smtm.api.ResourcePaths
import com.smtm.application.spring.conversions.Plans.toHalCollection
import com.smtm.core.api.PlansQueries
import com.smtm.core.domain.OwnerId
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping(ResourcePaths.PLAN_HEADERS)
class PlanHeadersEndpoint(
    private val plansQueries: PlansQueries,
    private val ownerIdProvider: () -> OwnerId,
    private val linksFactory: LinkFactory
) {

    @GetMapping(
        consumes = [MediaType.VERSION_1_JSON],
        produces = [MediaType.VERSION_1_JSON]
    )
    fun getAll(
        @RequestParam("matching-date", required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        matchingDate: LocalDate? = null
    ): ResponseEntity<HalCollection> {
        return PlansQueries.Criteria.by(ownerIdProvider(), matchingDate)
            .let { plansQueries.getPlanHeadersBy(it) }
            .map { it.toHalCollection(linksFactory) }
            .map { ResponseEntity.ok(it) }
            .getOrElse { throw it }
    }
}
