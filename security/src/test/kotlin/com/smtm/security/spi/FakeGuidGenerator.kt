package com.smtm.security.spi

class FakeGuidGenerator : GuidGenerator {

    var guid: String = ""

    override fun generate(): String = guid
}
