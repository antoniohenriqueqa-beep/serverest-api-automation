package br.com.qa.serverest.steps;

import br.com.qa.serverest.client.CarrinhoClient;
import br.com.qa.serverest.client.ProdutoClient;
import br.com.qa.serverest.support.ScenarioContext;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;

import static org.assertj.core.api.Assertions.assertThat;

public class CarrinhoSteps {

    private final CarrinhoClient carrinhoClient = new CarrinhoClient();
    private final ProdutoClient produtoClient = new ProdutoClient();

    @Quando("consulto a lista de carrinhos")
    public void consultoAListaDeCarrinhos() {
        ScenarioContext.set(ScenarioContext.RESPONSE, carrinhoClient.listar());
    }

    @Quando("registro um carrinho com {int} unidades do produto")
    public void registroUmCarrinhoComUnidadesDoProduto(int quantidade) {
        registrar(ScenarioContext.get(ScenarioContext.PRODUTO_ID), quantidade);
    }

    @Quando("registro um carrinho com {int} unidade do produto")
    public void registroUmCarrinhoComUmaUnidadeDoProduto(int quantidade) {
        registrar(ScenarioContext.get(ScenarioContext.PRODUTO_ID), quantidade);
    }

    @Quando("registro um carrinho com o produto de id {string}")
    public void registroUmCarrinhoComOProdutoDeId(String idProduto) {
        registrar(idProduto, 1);
    }

    @Quando("registro um carrinho sem token de autenticação")
    public void registroUmCarrinhoSemTokenDeAutenticacao() {
        String idProduto = ScenarioContext.get(ScenarioContext.PRODUTO_ID);
        ScenarioContext.set(ScenarioContext.RESPONSE, carrinhoClient.registrarSemToken(idProduto, 1));
    }

    @Dado("que registrei um carrinho com {int} unidade do produto")
    public void queRegistreiUmCarrinhoComUmaUnidadeDoProduto(int quantidade) {
        registrarComoPreCondicao(quantidade);
    }

    @Dado("que registrei um carrinho com {int} unidades do produto")
    public void queRegistreiUmCarrinhoComUnidadesDoProduto(int quantidade) {
        registrarComoPreCondicao(quantidade);
    }

    @Quando("concluo a compra do carrinho")
    public void concluoACompraDoCarrinho() {
        String token = ScenarioContext.get(ScenarioContext.TOKEN);
        ScenarioContext.set(ScenarioContext.RESPONSE, carrinhoClient.concluirCompra(token));
    }

    @Quando("cancelo a compra do carrinho")
    public void canceloACompraDoCarrinho() {
        String token = ScenarioContext.get(ScenarioContext.TOKEN);
        ScenarioContext.set(ScenarioContext.RESPONSE, carrinhoClient.cancelarCompra(token));
    }

    @Entao("a quantidade do produto em estoque deve ser {int}")
    public void aQuantidadeDoProdutoEmEstoqueDeveSer(int esperada) {
        String idProduto = ScenarioContext.get(ScenarioContext.PRODUTO_ID);
        Response consulta = produtoClient.buscarPorId(idProduto);

        assertThat(consulta.statusCode())
                .as("Consulta do produto apos a operacao no carrinho")
                .isEqualTo(200);

        assertThat(consulta.jsonPath().getInt("quantidade"))
                .as("Quantidade do produto em estoque")
                .isEqualTo(esperada);
    }

    private void registrar(String idProduto, int quantidade) {
        String token = ScenarioContext.get(ScenarioContext.TOKEN);
        ScenarioContext.set(ScenarioContext.RESPONSE, carrinhoClient.registrar(idProduto, quantidade, token));
    }

    private void registrarComoPreCondicao(int quantidade) {
        String idProduto = ScenarioContext.get(ScenarioContext.PRODUTO_ID);
        String token = ScenarioContext.get(ScenarioContext.TOKEN);
        Response response = carrinhoClient.registrar(idProduto, quantidade, token);
        if (response.statusCode() != 201) {
            throw new IllegalStateException(
                    "Pre-condicao falhou: nao foi possivel registrar o carrinho. Resposta: " + response.asString());
        }
        ScenarioContext.set(ScenarioContext.CARRINHO_ID, response.jsonPath().getString("_id"));
    }
}
