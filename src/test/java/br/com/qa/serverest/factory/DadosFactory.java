package br.com.qa.serverest.factory;

import br.com.qa.serverest.model.Produto;
import br.com.qa.serverest.model.Usuario;
import net.datafaker.Faker;

import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Gera massa de teste dinamica.
 * Nenhum dado fixo e usado em cadastro: a ServeRest e um ambiente
 * compartilhado e publico, entao e-mail e nome de produto precisam ser
 * unicos a cada execucao para o teste nao falhar por colisao.
 */
public final class DadosFactory {

    private static final Faker FAKER = new Faker(new Locale("pt", "BR"));

    private DadosFactory() {
    }

    public static Usuario usuarioAdministrador() {
        return novoUsuario("true");
    }

    public static Usuario usuarioComum() {
        return novoUsuario("false");
    }

    private static Usuario novoUsuario(String administrador) {
        return new Usuario(
                FAKER.name().fullName(),
                emailUnico(),
                FAKER.internet().password(8, 12),
                administrador
        );
    }

    public static Produto produtoValido() {
        return new Produto(
                FAKER.commerce().productName() + " " + sufixoUnico(),
                ThreadLocalRandom.current().nextInt(10, 5000),
                FAKER.lorem().sentence(6),
                ThreadLocalRandom.current().nextInt(1, 100)
        );
    }

    private static String emailUnico() {
        return String.format("qa.%s@teste.com", sufixoUnico());
    }

    private static String sufixoUnico() {
        return System.currentTimeMillis() + "" + ThreadLocalRandom.current().nextInt(1000, 9999);
    }
}
