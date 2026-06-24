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
    public String getNome() { return nome; }
    public String getPersonalidade() { return personalidade; }
    public String getMotivacao() { return motivacao; }
    public void setMotivacao(String m) { this.motivacao = m; }
    public String getAlibi() { return alibi; }
    public void setAlibi(String a) { this.alibi = a; }
    public boolean isCulpado() { return culpado; }
    public void setCulpado(boolean c) { this.culpado = c; }
    public String toString() { return nome; }
}
