package com.smtm.application.security

import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.GetMapping

@RestController
@RequestMapping(path = ["/test/protected/resource"])
class TestProtectedResourceController {

    @GetMapping
    fun test(): String {
        return "test"
    }
}
