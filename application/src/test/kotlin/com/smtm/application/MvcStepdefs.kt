package com.smtm.application

import io.cucumber.java.ParameterType
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

class MvcStepdefs(private val mvc: MockMvc) {

    private val headers: HttpHeaders = HttpHeaders()
    private lateinit var actions: ResultActions

    @Given("request headers are")
    fun requestHeadersAre(headers: Map<String, String>) {
        headers.forEach { (headerName, headerValue) -> this.headers.add(headerName, headerValue) }
    }

    @When("client performs a {httpMethod} {string} request with body")
    fun clientPerformsARequestWithBody(method: HttpMethod, endpoint: String, body: String) {
        actions = mvc.perform(
            request(method, endpoint)
                .headers(headers)
                .content(body)
        )
    }

    @When("client performs a {httpMethod} {string} request")
    fun clientPerformsARequest(method: HttpMethod, endpoint: String) {
        actions = mvc.perform(
            request(method, endpoint)
                .headers(headers)
        )
    }

    @Then("response status code is {int}")
    fun responseStatusCodeIs(code: Int) {
        actions.andExpect(status().`is`(code))
    }

    @Then("response headers are")
    fun responseHeadersAre(headers: Map<String, String>) {
        headers.forEach { (name, value) -> actions.andExpect(header().string(name, value)) }
    }

    @Then("response body is")
    fun responseBodyIs(body: String) {
        actions.andExpect(content().json(body))
    }

    @ParameterType("(GET|POST|PUT|DELETE|OPTIONS|TRACE|PATCH)")
    fun httpMethod(method: String): HttpMethod {
        return HttpMethod.valueOf(method)
    }

}
