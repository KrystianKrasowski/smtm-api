package com.smtm.application;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Map;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
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

    @When("client calls {string} with body")
    public void clientCallsAddressWithBody(String address, String body) throws Exception {
        actions = mvc.perform(
            post(address)
                .headers(headers)
                .content(body)
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

    private void assertHeader(String name, String value) {
        try {
            actions.andExpect(header().string(name, value));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
