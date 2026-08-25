package br.com.qa.serverest.support;

import br.com.qa.serverest.config.ConfigManager;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

/**
 * Centraliza a montagem da request specification.
 * Toda requisicao passa por aqui, o que garante base URI unica,
 * content type padrao e anexo automatico de request/response no Allure.
 */
public final class SpecFactory {

    private SpecFactory() {
    }

    public static RequestSpecification base() {
        return new RequestSpecBuilder()
                .setBaseUri(ConfigManager.baseUri())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .build();
    }

    public static RequestSpecification autenticada(String token) {
        return base().header("Authorization", token);
    }
}
