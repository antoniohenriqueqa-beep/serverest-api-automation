package br.com.qa.serverest.steps;

import br.com.qa.serverest.client.AuthClient;
import br.com.qa.serverest.model.Credenciais;
import br.com.qa.serverest.model.Usuario;
import br.com.qa.serverest.support.ScenarioContext;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;

import static org.assertj.core.api.Assertions.assertThat;

public class AutenticacaoSteps {

    private final AuthClient authClient = new AuthClient();

    @Quando("realizo o login com as credenciais do usuário")
    public void realizoOLoginComAsCredenciaisDoUsuario() {
        Usuario usuario = ScenarioContext.get(ScenarioContext.USUARIO);
        autenticar(usuario.getEmail(), usuario.getPassword());
    }

    @Quando("realizo o login com a senha {string}")
    public void realizoOLoginComASenha(String senha) {
        Usuario usuario = ScenarioContext.get(ScenarioContext.USUARIO);
        autenticar(usuario.getEmail(), senha);
    }

    @Quando("realizo o login com o e-mail {string} e a senha {string}")
    public void realizoOLoginComOEmailEASenha(String email, String senha) {
        autenticar(email, senha);
    }

    @Entao("um token de autorização deve ser retornado")
    public void umTokenDeAutorizacaoDeveSerRetornado() {
        Response response = ScenarioContext.get(ScenarioContext.RESPONSE);
        assertThat(response.jsonPath().getString("authorization"))
                .as("Token de autorizacao")
                .isNotBlank()
                .startsWith("Bearer ");
    }

    private void autenticar(String email, String senha) {
        Response response = authClient.autenticar(new Credenciais(email, senha));
        ScenarioContext.set(ScenarioContext.RESPONSE, response);
        if (response.statusCode() == 200) {
            ScenarioContext.set(ScenarioContext.TOKEN, response.jsonPath().getString("authorization"));
        }
    }
}
