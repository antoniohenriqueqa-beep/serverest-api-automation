package br.com.qa.serverest.support;

import java.util.HashMap;
import java.util.Map;

/**
 * Estado compartilhado entre steps do mesmo cenario.
 * Usa ThreadLocal para permitir execucao paralela sem vazamento
 * de dados entre cenarios.
 */
public final class ScenarioContext {

    public static final String RESPONSE = "response";
    public static final String USUARIO = "usuario";
    public static final String PRODUTO = "produto";
    public static final String TOKEN = "token";
    public static final String USUARIO_ID = "usuarioId";
    public static final String PRODUTO_ID = "produtoId";
    public static final String CARRINHO_ID = "carrinhoId";

    private static final ThreadLocal<Map<String, Object>> CONTEXT =
            ThreadLocal.withInitial(HashMap::new);

    private ScenarioContext() {
    }

    public static void set(String key, Object value) {
        CONTEXT.get().put(key, value);
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(String key) {
        Object value = CONTEXT.get().get(key);
        if (value == null) {
            throw new IllegalStateException("Chave ausente no contexto do cenario: " + key);
        }
        return (T) value;
    }

    public static boolean contains(String key) {
        return CONTEXT.get().containsKey(key);
    }

    public static void clear() {
        CONTEXT.get().clear();
        CONTEXT.remove();
    }
}
