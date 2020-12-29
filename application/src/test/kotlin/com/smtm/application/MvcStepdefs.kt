package com.smtm.application;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class MvcStepdefs {

    private final MockMvc mvc;
    private final HttpHeaders headers;

    private ResultActions actions;

    public MvcStepdefs(MockMvc mvc) {
        this.mvc = mvc;
        this.headers = new HttpHeaders();
    }

    @Given("request headers are")
    public void requestHeadersAre(Map<String, String> headers) {
        headers.forEach(this.headers::add);
    }

    @When("client performs a {httpMethod} {string} request with body")
    public void clientPerformsARequestWithBody(HttpMethod method, String endpoint, String body) throws Exception {
        actions = mvc.perform(
            request(method, endpoint)
                .headers(headers)
                .content(body)
        );
    }

    @When("client performs a {httpMethod} {string} request")
    public void clientPerformsARequest(HttpMethod method, String endpoint) throws Exception {
        actions = mvc.perform(
            request(method, endpoint)
                .headers(headers)
        );
    }

    @Then("response status code is {int}")
    public void responseStatusCodeIs(Integer code) throws Exception {
        actions.andExpect(status().is(code));
    }

    @Then("response headers are")
    public void responseHeadersAre(Map<String, String> headers) {
        headers.forEach(this::assertHeader);
    }

    @Then("response body is")
    public void responseBodyIs(String body) throws Exception {
        actions.andExpect(content().json(body));
    }

    @ParameterType("(GET|POST|PUT|DELETE|OPTIONS|TRACE|PATCH)")
    public HttpMethod httpMethod(String method) {
        return HttpMethod.resolve(method);
    }

    private void assertHeader(String name, String value) {
        try {
            actions.andExpect(header().string(name, value));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
