package com.smtm.application

abstract class HalResource(open val links: Map<String, Link> = emptyMap())
