package com.crimevariavel.model;
public class Jogador {
    private int id;
    private String nome;
    private int moedas;
    private int pontosUpgrade;
    private int casosResolvidos;

    public Jogador() {}
    public Jogador(int id, String nome, int moedas, int pontosUpgrade, int casosResolvidos) {
        this.id = id; this.nome = nome; this.moedas = moedas;
        this.pontosUpgrade = pontosUpgrade; this.casosResolvidos = casosResolvidos;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public int getMoedas() { return moedas; }
    public void setMoedas(int moedas) { this.moedas = moedas; }
    public int getPontosUpgrade() { return pontosUpgrade; }
    public void setPontosUpgrade(int p) { this.pontosUpgrade = p; }
    public int getCasosResolvidos() { return casosResolvidos; }
    public void setCasosResolvidos(int c) { this.casosResolvidos = c; }
    public String toString() { return "Jogador{id=" + id + ", nome='" + nome + "'}"; }
}
