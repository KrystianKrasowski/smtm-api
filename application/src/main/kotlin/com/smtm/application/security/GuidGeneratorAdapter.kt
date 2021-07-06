package com.smtm.application.security

import com.smtm.security.spi.GuidGenerator
import java.util.*

class GuidGeneratorAdapter : GuidGenerator {

    override fun generate(): String {
        return UUID.randomUUID().toString()
    }
}
