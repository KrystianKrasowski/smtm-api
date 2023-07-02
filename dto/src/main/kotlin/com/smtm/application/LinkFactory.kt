package com.smtm.application

class LinkFactory(private val schema: String, private val address: String, private val port: Int) {

    fun create(path: String) = Link(href = "$schema://$address:$port$path")
}