package br.com.qa.serverest.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Carrega a configuracao do ambiente alvo.
 * O ambiente e definido pela system property "env" (default: hml),
 * permitindo executar contra o serverest.dev publico ou contra uma
 * instancia local em Docker sem alterar codigo.
 */
public final class ConfigManager {

    private static final Properties PROPERTIES = new Properties();

    static {
        String env = System.getProperty("env", "hml");
        String file = String.format("config/%s.properties", env);

        try (InputStream input = ConfigManager.class.getClassLoader().getResourceAsStream(file)) {
            if (input == null) {
                throw new IllegalStateException("Arquivo de configuracao nao encontrado: " + file);
            }
            PROPERTIES.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Falha ao carregar a configuracao do ambiente: " + env, e);
        }
    }

    private ConfigManager() {
    }

    public static String baseUri() {
        return get("base.uri");
    }

    public static int timeoutMs() {
        return Integer.parseInt(get("timeout.ms"));
    }

    private static String get(String key) {
        String value = System.getProperty(key, PROPERTIES.getProperty(key));
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Propriedade nao configurada: " + key);
        }
        return value;
    }
}
