package com.smtm.api

abstract class HalCollection(
    open val links: Map<String, Link>,
    open val count: Int,
    open val total: Int,
    open val embedded: Map<String, Iterable<HalResource<out Any>>>
)

