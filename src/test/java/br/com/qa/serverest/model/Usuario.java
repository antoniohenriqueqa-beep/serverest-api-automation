package br.com.qa.serverest.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Usuario {

    private String nome;
    private String email;
    private String password;
    private String administrador;

    public Usuario() {
    }

    public Usuario(String nome, String email, String password, String administrador) {
        this.nome = nome;
        this.email = email;
        this.password = password;
        this.administrador = administrador;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAdministrador() {
        return administrador;
    }

    public void setAdministrador(String administrador) {
        this.administrador = administrador;
    }

    /** Remove um campo obrigatorio para cenarios negativos de contrato. */
    public Usuario semCampo(String campo) {
        switch (campo) {
            case "nome" -> this.nome = null;
            case "email" -> this.email = null;
            case "password" -> this.password = null;
            case "administrador" -> this.administrador = null;
            default -> throw new IllegalArgumentException("Campo desconhecido: " + campo);
        }
        return this;
    }
}
