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
    
    public List<Suspeito> getSuspeitos(){
        return suspeitos; }
    
    public List<Pista> getPistas(){
        return pistas; }
    
    public Suspeito getCulpado(){ 
        return culpado; }
    
    public String getMotivacaoVerdadeira(){ 
        return motivacaoVerdadeira; }
    
    public String getLinhaDoTempo(){ 
        return linhaDoTempo; }
    
    public String getDificuldade(){ 
        return dificuldade; }
}
