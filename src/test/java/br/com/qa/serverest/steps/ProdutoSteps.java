package br.com.qa.serverest.steps;

import br.com.qa.serverest.client.AuthClient;
import br.com.qa.serverest.client.ProdutoClient;
import br.com.qa.serverest.client.UsuarioClient;
import br.com.qa.serverest.factory.DadosFactory;
import br.com.qa.serverest.model.Credenciais;
import br.com.qa.serverest.model.Produto;
import br.com.qa.serverest.model.Usuario;
import br.com.qa.serverest.support.ScenarioContext;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;

import static org.assertj.core.api.Assertions.assertThat;

public class ProdutoSteps {

    private final ProdutoClient produtoClient = new ProdutoClient();
    private final UsuarioClient usuarioClient = new UsuarioClient();
    private final AuthClient authClient = new AuthClient();

    @Dado("que estou autenticado como administrador")
    public void queEstouAutenticadoComoAdministrador() {
        autenticarNovoUsuario(DadosFactory.usuarioAdministrador());
    }

    @Dado("que estou autenticado como usuário comum")
    public void queEstouAutenticadoComoUsuarioComum() {
        autenticarNovoUsuario(DadosFactory.usuarioComum());
    }

    @Dado("que possuo os dados de um novo produto")
    public void quePossuoOsDadosDeUmNovoProduto() {
        ScenarioContext.set(ScenarioContext.PRODUTO, DadosFactory.produtoValido());
    }

    @Quando("consulto a lista de produtos")
    public void consultoAListaDeProdutos() {
        ScenarioContext.set(ScenarioContext.RESPONSE, produtoClient.listar());
    }

    @Quando("consulto o produto de id {string}")
    public void consultoOProdutoDeId(String id) {
        ScenarioContext.set(ScenarioContext.RESPONSE, produtoClient.buscarPorId(id));
    }

    @Quando("consulto o produto recém-cadastrado pelo seu id")
    public void consultoOProdutoRecemCadastradoPeloSeuId() {
        String id = ScenarioContext.get(ScenarioContext.PRODUTO_ID);
        ScenarioContext.set(ScenarioContext.RESPONSE, produtoClient.buscarPorId(id));
    }

    // A busca por id so tem valor se confirmar que veio o produto certo.
    // Validar apenas o status code deixaria passar uma API que responde
    // 200 com o registro errado -- falha silenciosa e dificil de rastrear
    // depois, porque o teste continua verde.
    @Entao("o produto retornado deve ser o que foi cadastrado")
    public void oProdutoRetornadoDeveSerOQueFoiCadastrado() {
        Produto esperado = ScenarioContext.get(ScenarioContext.PRODUTO);
        String idEsperado = ScenarioContext.get(ScenarioContext.PRODUTO_ID);
        Response response = ScenarioContext.get(ScenarioContext.RESPONSE);

        assertThat(response.jsonPath().getString("_id"))
                .as("Identificador do produto retornado")
                .isEqualTo(idEsperado);
        assertThat(response.jsonPath().getString("nome"))
                .as("Nome do produto retornado")
                .isEqualTo(esperado.getNome());
        assertThat(response.jsonPath().getInt("preco"))
                .as("Preco do produto retornado")
                .isEqualTo(esperado.getPreco());
    }

    @Quando("submeto o cadastro do produto")
    public void submetoOCadastroDoProduto() {
        Produto produto = ScenarioContext.get(ScenarioContext.PRODUTO);
        String token = ScenarioContext.get(ScenarioContext.TOKEN);
        ScenarioContext.set(ScenarioContext.RESPONSE, produtoClient.cadastrar(produto, token));
    }

    @Quando("submeto o cadastro do produto sem token de autenticação")
    public void submetoOCadastroDoProdutoSemToken() {
        Produto produto = ScenarioContext.get(ScenarioContext.PRODUTO);
        ScenarioContext.set(ScenarioContext.RESPONSE, produtoClient.cadastrarSemToken(produto));
    }

    @Dado("que existe um produto cadastrado")
    public void queExisteUmProdutoCadastrado() {
        cadastrarComoPreCondicao(DadosFactory.produtoValido());
    }

    @Dado("que existe um produto cadastrado com {int} unidades em estoque")
    public void queExisteUmProdutoCadastradoComUnidadesEmEstoque(int quantidade) {
        Produto produto = DadosFactory.produtoValido();
        produto.setQuantidade(quantidade);
        cadastrarComoPreCondicao(produto);
    }

    private void cadastrarComoPreCondicao(Produto produto) {
        String token = ScenarioContext.get(ScenarioContext.TOKEN);
        Response response = produtoClient.cadastrar(produto, token);
        if (response.statusCode() != 201) {
            throw new IllegalStateException(
                    "Pre-condicao falhou: nao foi possivel cadastrar o produto. Resposta: " + response.asString());
        }
        ScenarioContext.set(ScenarioContext.PRODUTO, produto);
        ScenarioContext.set(ScenarioContext.PRODUTO_ID, response.jsonPath().getString("_id"));
    }

    @Quando("submeto o cadastro de outro produto com o mesmo nome")
    public void submetoOCadastroDeOutroProdutoComOMesmoNome() {
        Produto existente = ScenarioContext.get(ScenarioContext.PRODUTO);
        Produto duplicado = DadosFactory.produtoValido();
        duplicado.setNome(existente.getNome());
        String token = ScenarioContext.get(ScenarioContext.TOKEN);
        ScenarioContext.set(ScenarioContext.RESPONSE, produtoClient.cadastrar(duplicado, token));
    }

    private void autenticarNovoUsuario(Usuario usuario) {
        Response cadastro = usuarioClient.cadastrar(usuario);
        if (cadastro.statusCode() != 201) {
            throw new IllegalStateException(
                    "Pre-condicao falhou ao cadastrar usuario. Resposta: " + cadastro.asString());
        }
        Response login = authClient.autenticar(new Credenciais(usuario.getEmail(), usuario.getPassword()));
        if (login.statusCode() != 200) {
            throw new IllegalStateException(
                    "Pre-condicao falhou ao autenticar usuario. Resposta: " + login.asString());
        }
        ScenarioContext.set(ScenarioContext.USUARIO, usuario);
        ScenarioContext.set(ScenarioContext.TOKEN, login.jsonPath().getString("authorization"));
    }
}
