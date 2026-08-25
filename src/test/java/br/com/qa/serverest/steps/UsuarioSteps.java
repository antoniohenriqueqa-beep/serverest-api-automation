package br.com.qa.serverest.steps;

import br.com.qa.serverest.client.UsuarioClient;
import br.com.qa.serverest.factory.DadosFactory;
import br.com.qa.serverest.model.Usuario;
import br.com.qa.serverest.support.ScenarioContext;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;

public class UsuarioSteps {

    private final UsuarioClient usuarioClient = new UsuarioClient();

    @Quando("consulto a lista de usuários")
    public void consultoAListaDeUsuarios() {
        ScenarioContext.set(ScenarioContext.RESPONSE, usuarioClient.listar());
    }

    @Quando("consulto o usuário de id {string}")
    public void consultoOUsuarioDeId(String id) {
        ScenarioContext.set(ScenarioContext.RESPONSE, usuarioClient.buscarPorId(id));
    }

    @Dado("que possuo os dados de um novo usuário administrador")
    public void quePossuoOsDadosDeUmNovoUsuarioAdministrador() {
        ScenarioContext.set(ScenarioContext.USUARIO, DadosFactory.usuarioAdministrador());
    }

    @Quando("submeto o cadastro do usuário")
    public void submetoOCadastroDoUsuario() {
        Usuario usuario = ScenarioContext.get(ScenarioContext.USUARIO);
        ScenarioContext.set(ScenarioContext.RESPONSE, usuarioClient.cadastrar(usuario));
    }

    @Quando("submeto o cadastro do usuário sem o campo {string}")
    public void submetoOCadastroDoUsuarioSemOCampo(String campo) {
        Usuario usuario = ScenarioContext.get(ScenarioContext.USUARIO);
        ScenarioContext.set(ScenarioContext.RESPONSE, usuarioClient.cadastrar(usuario.semCampo(campo)));
    }

    @Dado("que existe um usuário administrador cadastrado")
    public void queExisteUmUsuarioAdministradorCadastrado() {
        cadastrarEArmazenar(DadosFactory.usuarioAdministrador());
    }

    @Dado("que existe um usuário comum cadastrado")
    public void queExisteUmUsuarioComumCadastrado() {
        cadastrarEArmazenar(DadosFactory.usuarioComum());
    }

    @Quando("submeto o cadastro de outro usuário com o mesmo e-mail")
    public void submetoOCadastroDeOutroUsuarioComOMesmoEmail() {
        Usuario existente = ScenarioContext.get(ScenarioContext.USUARIO);
        Usuario duplicado = DadosFactory.usuarioAdministrador();
        duplicado.setEmail(existente.getEmail());
        ScenarioContext.set(ScenarioContext.RESPONSE, usuarioClient.cadastrar(duplicado));
    }

    private void cadastrarEArmazenar(Usuario usuario) {
        Response response = usuarioClient.cadastrar(usuario);
        if (response.statusCode() != 201) {
            throw new IllegalStateException(
                    "Pre-condicao falhou: nao foi possivel cadastrar o usuario. Resposta: " + response.asString());
        }
        ScenarioContext.set(ScenarioContext.USUARIO, usuario);
        ScenarioContext.set(ScenarioContext.USUARIO_ID, response.jsonPath().getString("_id"));
        ScenarioContext.set(ScenarioContext.RESPONSE, response);
    }
}
