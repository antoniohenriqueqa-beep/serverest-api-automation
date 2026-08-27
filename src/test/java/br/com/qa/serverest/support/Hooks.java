package br.com.qa.serverest.support;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.restassured.RestAssured;

public class Hooks {

    @Before
    public void setUp() {
        // Log apenas quando a assercao falha, que e quando ele serve
        // para alguma coisa. Registrar RequestLoggingFilter e
        // ResponseLoggingFilter aqui despejaria request e response de
        // todos os cenarios, inclusive dos que passam: eram 10.716 das
        // 24.583 linhas do build, e o efeito pratico e que a falha real
        // fica enterrada no dump dos cenarios que deram certo.
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
