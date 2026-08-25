package br.com.qa.serverest.steps;

import br.com.qa.serverest.support.ScenarioContext;
import io.cucumber.java.pt.Entao;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Steps reutilizaveis por qualquer feature.
 * Assercao de status, contrato e mensagem ficam concentradas aqui
 * para nao serem reescritas em cada arquivo de step.
 */
public class CommonSteps {

    @Entao("o status code deve ser {int}")
    public void oStatusCodeDeveSer(int esperado) {
        Response response = ScenarioContext.get(ScenarioContext.RESPONSE);
        assertThat(response.statusCode())
                .as("Status code retornado pela API")
                .isEqualTo(esperado);
    }

    @Entao("a mensagem retornada deve ser {string}")
    public void aMensagemRetornadaDeveSer(String esperada) {
        Response response = ScenarioContext.get(ScenarioContext.RESPONSE);
        assertThat(response.jsonPath().getString("message"))
                .as("Mensagem de negocio retornada pela API")
                .isEqualTo(esperada);
    }

    @Entao("o corpo da resposta deve seguir o schema {string}")
    public void oCorpoDaRespostaDeveSeguirOSchema(String schema) {
        Response response = ScenarioContext.get(ScenarioContext.RESPONSE);
        response.then().assertThat()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/" + schema + ".json"));
    }

    @Entao("o identificador do recurso criado deve ser retornado")
    public void oIdentificadorDoRecursoCriadoDeveSerRetornado() {
        Response response = ScenarioContext.get(ScenarioContext.RESPONSE);
        assertThat(response.jsonPath().getString("_id"))
                .as("Identificador do recurso criado")
                .isNotBlank();
    }

    @Entao("o corpo deve conter o erro {string} para o campo {string}")
    public void oCorpoDeveConterOErroParaOCampo(String mensagem, String campo) {
        Response response = ScenarioContext.get(ScenarioContext.RESPONSE);
        assertThat(response.jsonPath().getString(campo))
                .as("Mensagem de validacao do campo %s", campo)
                .isEqualTo(mensagem);
    }
}
