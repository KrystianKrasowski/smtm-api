package com.smtm.api

abstract class HalResource(open val links: Map<String, Link> = emptyMap())
