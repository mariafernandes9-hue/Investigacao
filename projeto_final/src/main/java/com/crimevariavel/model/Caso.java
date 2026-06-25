package com.crimevariavel.model;

import java.util.List;

public class Caso {
    
    private List<Suspeito> suspeitos;
    private List<Pista> pistas;
    private Suspeito culpado;
    private String motivacaoVerdadeira;
    private String linhaDoTempo;
    private String dificuldade;

    public Caso(List<Suspeito> suspeitos, List<Pista> pistas, Suspeito culpado,
                String motivacao, String linhaDoTempo, String dificuldade) {
        this.suspeitos = suspeitos; this.pistas = pistas; this.culpado = culpado;
        this.motivacaoVerdadeira = motivacao; this.linhaDoTempo = linhaDoTempo;
        this.dificuldade = dificuldade;
    }
    
    public List<Suspeito> getSuspeitos() {
        return suspeitos;
    }

    public void setSuspeitos(List<Suspeito> suspeitos) {
        this.suspeitos = suspeitos;
    }

    public List<Pista> getPistas() {
        return pistas;
    }

    public void setPistas(List<Pista> pistas) {
        this.pistas = pistas;
    }

    public Suspeito getCulpado() {
        return culpado;
    }

    public void setCulpado(Suspeito culpado) {
        this.culpado = culpado;
    }

    public String getMotivacaoVerdadeira() {
        return motivacaoVerdadeira;
    }

    public void setMotivacaoVerdadeira(String motivacaoVerdadeira) {
        this.motivacaoVerdadeira = motivacaoVerdadeira;
    }

    public String getLinhaDoTempo() {
        return linhaDoTempo;
    }

    public void setLinhaDoTempo(String linhaDoTempo) {
        this.linhaDoTempo = linhaDoTempo;
    }

    public String getDificuldade() {
        return dificuldade;
    }

    public void setDificuldade(String dificuldade) {
        this.dificuldade = dificuldade;
    }
}
