package br.com.qa.serverest.client;

import br.com.qa.serverest.model.Produto;
import br.com.qa.serverest.support.SpecFactory;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class ProdutoClient {

    private static final String PRODUTOS = "/produtos";

    public Response listar() {
        return given().spec(SpecFactory.base())
                .when().get(PRODUTOS);
    }

    public Response buscarPorId(String id) {
        return given().spec(SpecFactory.base())
                .pathParam("id", id)
                .when().get(PRODUTOS + "/{id}");
    }

    public Response cadastrar(Produto produto, String token) {
        return given().spec(SpecFactory.autenticada(token))
                .body(produto)
                .when().post(PRODUTOS);
    }

    public Response cadastrarSemToken(Produto produto) {
        return given().spec(SpecFactory.base())
                .body(produto)
                .when().post(PRODUTOS);
    }

    public Response excluir(String id, String token) {
        return given().spec(SpecFactory.autenticada(token))
                .pathParam("id", id)
                .when().delete(PRODUTOS + "/{id}");
    }
}
