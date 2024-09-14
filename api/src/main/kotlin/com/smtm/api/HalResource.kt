package com.smtm.api

abstract class HalResource<ID>(
    open val links: Map<String, Link> = emptyMap(),
    open val id: ID,
)
