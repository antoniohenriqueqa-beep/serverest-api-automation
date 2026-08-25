package br.com.qa.serverest.client;

import br.com.qa.serverest.support.SpecFactory;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * Camada de acesso ao recurso /carrinhos.
 */
public class CarrinhoClient {

    private static final String CARRINHOS = "/carrinhos";

    public Response listar() {
        return given().spec(SpecFactory.base())
                .when().get(CARRINHOS);
    }

    public Response buscarPorId(String id) {
        return given().spec(SpecFactory.base())
                .pathParam("id", id)
                .when().get(CARRINHOS + "/{id}");
    }

    public Response registrar(String idProduto, int quantidade, String token) {
        return given().spec(SpecFactory.autenticada(token))
                .body(montarBody(idProduto, quantidade))
                .when().post(CARRINHOS);
    }

    public Response registrarSemToken(String idProduto, int quantidade) {
        return given().spec(SpecFactory.base())
                .body(montarBody(idProduto, quantidade))
                .when().post(CARRINHOS);
    }

    public Response concluirCompra(String token) {
        return given().spec(SpecFactory.autenticada(token))
                .when().delete(CARRINHOS + "/concluir-compra");
    }

    public Response cancelarCompra(String token) {
        return given().spec(SpecFactory.autenticada(token))
                .when().delete(CARRINHOS + "/cancelar-compra");
    }

    private Map<String, Object> montarBody(String idProduto, int quantidade) {
        return Map.of(
                "produtos", List.of(Map.of("idProduto", idProduto, "quantidade", quantidade))
        );
    }
}
