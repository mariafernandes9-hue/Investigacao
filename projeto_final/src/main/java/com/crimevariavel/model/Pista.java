package com.crimevariavel.model;
public class Pista {
    public enum Tipo { VERDADEIRA, FALSA, INDIRETA }

    private String descricao;
    private Tipo tipo;
    private String local;

    public Pista(String descricao, Tipo tipo, String local) {
        this.descricao = descricao; this.tipo = tipo; this.local = local;
    }
    public String getDescricao() { return descricao; }
    public Tipo getTipo() { return tipo; }
    public String getLocal() { return local; }
}
