package br.com.qa.serverest.client;

import br.com.qa.serverest.model.Credenciais;
import br.com.qa.serverest.support.SpecFactory;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class AuthClient {

    private static final String LOGIN = "/login";

    public Response autenticar(Credenciais credenciais) {
        return given().spec(SpecFactory.base())
                .body(credenciais)
                .when().post(LOGIN);
    }
}
