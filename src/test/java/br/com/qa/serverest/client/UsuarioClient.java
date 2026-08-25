package br.com.qa.serverest.client;

import br.com.qa.serverest.model.Usuario;
import br.com.qa.serverest.support.SpecFactory;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

/**
 * Camada de acesso ao recurso /usuarios.
 * Os steps nunca montam requisicao diretamente: eles chamam o client.
 * Se a rota ou o header mudarem, o ajuste acontece em um unico lugar.
 */
public class UsuarioClient {

    private static final String USUARIOS = "/usuarios";

    public Response listar() {
        return given().spec(SpecFactory.base())
                .when().get(USUARIOS);
    }

    public Response buscarPorId(String id) {
        return given().spec(SpecFactory.base())
                .pathParam("id", id)
                .when().get(USUARIOS + "/{id}");
    }

    public Response cadastrar(Usuario usuario) {
        return given().spec(SpecFactory.base())
                .body(usuario)
                .when().post(USUARIOS);
    }

    public Response excluir(String id) {
        return given().spec(SpecFactory.base())
                .pathParam("id", id)
                .when().delete(USUARIOS + "/{id}");
    }
}
