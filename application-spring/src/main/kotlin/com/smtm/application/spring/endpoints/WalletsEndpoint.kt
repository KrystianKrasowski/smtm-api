package com.smtm.application.spring.endpoints

import arrow.core.getOrElse
import com.smtm.api.LinkFactory
import com.smtm.api.MediaType
import com.smtm.api.ResourcePaths
import com.smtm.api.v1.ApiProblemDto
import com.smtm.api.v1.WalletDto
import com.smtm.api.v1.WalletResource
import com.smtm.api.v1.WalletsCollection
import com.smtm.application.spring.conversions.Wallets.toDomain
import com.smtm.application.spring.conversions.Wallets.toHalCollection
import com.smtm.application.spring.conversions.Wallets.toResource
import com.smtm.application.spring.endpoints.exceptions.WalletsProblemException
import com.smtm.core.api.WalletsApi
import com.smtm.core.domain.EntityId
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
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

    @PostMapping(
        consumes = [MediaType.VERSION_1_JSON],
        produces = [MediaType.VERSION_1_JSON]
    )
    fun create(@RequestBody walletDto: WalletDto): ResponseEntity<WalletResource> =
        walletsApi.create(walletDto.toDomain())
            .map { it.toResource(linkFactory) }
            .map { ResponseEntity.created(it.getSelfURI()).body(it) }
            .getOrElse { throw WalletsProblemException(it) }

    @PutMapping(
        path = ["/{id}"],
        consumes = [MediaType.VERSION_1_JSON],
        produces = [MediaType.VERSION_1_JSON]
    )
    fun update(
        @PathVariable("id") id: String,
        @RequestBody walletDto: WalletDto
    ): ResponseEntity<WalletResource> =
        walletsApi.update(walletDto.toDomain(id))
            .map { it.toResource(linkFactory) }
            .map { ResponseEntity.ok(it) }
            .getOrElse { throw WalletsProblemException(it) }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: String): ResponseEntity<Nothing> =
        walletsApi.delete(EntityId.of(id))
            .map { ResponseEntity.noContent().build<Nothing>() }
            .getOrElse { throw WalletsProblemException(it) }

    @ExceptionHandler
    fun handleCategoriesProblemException(exception: WalletsProblemException): ResponseEntity<ApiProblemDto> =
        exception.toResponseEntity()
}
