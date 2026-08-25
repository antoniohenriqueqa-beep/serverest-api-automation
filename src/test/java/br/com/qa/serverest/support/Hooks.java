package br.com.qa.serverest.support;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;

public class Hooks {

    @Before
    public void setUp() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed() && ScenarioContext.contains(ScenarioContext.RESPONSE)) {
            io.restassured.response.Response response = ScenarioContext.get(ScenarioContext.RESPONSE);
            scenario.attach(response.asPrettyString().getBytes(), "application/json", "Resposta da API");
        }
        ScenarioContext.clear();
    }
}
