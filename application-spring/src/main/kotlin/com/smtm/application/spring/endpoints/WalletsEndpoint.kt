package com.smtm.application.spring.endpoints

import arrow.core.getOrElse
import com.smtm.api.LinkFactory
import com.smtm.api.MediaType
import com.smtm.api.ResourcePaths
import com.smtm.api.v1.ApiProblemDto
import com.smtm.api.v1.WalletsCollection
import com.smtm.application.spring.conversions.Wallets.toHalCollection
import com.smtm.application.spring.endpoints.exceptions.WalletsProblemException
import com.smtm.core.api.WalletsApi
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(ResourcePaths.WALLETS)
class WalletsEndpoint(
    private val walletsApi: WalletsApi,
    private val linkFactory: LinkFactory
) {

    @GetMapping(
        produces = [MediaType.VERSION_1_JSON]
    )
    fun getAll(): ResponseEntity<WalletsCollection> =
        walletsApi.getAll()
            .map { it.toHalCollection(linkFactory) }
            .map { ResponseEntity.ok(it) }
            .getOrElse { throw WalletsProblemException(it) }

    @ExceptionHandler
    fun handleCategoriesProblemException(exception: WalletsProblemException): ResponseEntity<ApiProblemDto> =
        exception.toResponseEntity()
}
