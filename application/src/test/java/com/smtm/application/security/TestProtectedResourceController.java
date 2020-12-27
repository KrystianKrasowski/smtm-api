package com.smtm.application.security;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/test/protected/resource")
public class TestProtectedResourceController {

    @GetMapping
    public String test() {
        return "test";
    }
}
