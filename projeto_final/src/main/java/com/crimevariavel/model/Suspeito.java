package com.crimevariavel.model;
public class Suspeito {
    private String nome;
    private String personalidade;
    private String motivacao;
    private String alibi;
    private boolean culpado;

    public Suspeito(String nome, String personalidade) {
        this.nome = nome; this.personalidade = personalidade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPersonalidade() {
        return personalidade;
    }

    public void setPersonalidade(String personalidade) {
        this.personalidade = personalidade;
    }

    public String getMotivacao() {
        return motivacao;
    }

    public void setMotivacao(String motivacao) {
        this.motivacao = motivacao;
    }

    public String getAlibi() {
        return alibi;
    }

    public void setAlibi(String alibi) {
        this.alibi = alibi;
    }

    public boolean isCulpado() {
        return culpado;
    }

    public void setCulpado(boolean culpado) {
        this.culpado = culpado;
    }

    
    public String toString() {
        return nome; }
}
