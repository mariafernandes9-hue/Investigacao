package com.crimevariavel.controller;

import com.crimevariavel.dao.JogadorDAO;
import com.crimevariavel.model.Jogador;
import com.crimevariavel.util.SceneManager;
import com.crimevariavel.util.SessaoJogador;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
 
public class LojaController {
 
    @FXML 
    private Label labelMoedas;
    
    @FXML 
    private Label labelStatus;
 
    private Jogador jogador;
    private JogadorDAO dao;
 
    @FXML
    public void initialize() {
        jogador = SessaoJogador.getJogador();
        dao = new JogadorDAO();
        atualizarMoedas();
    }
 
    private void atualizarMoedas() {
        labelMoedas.setText("Moedas: " + jogador.getMoedas());
    }
 
    private void comprar(String nomeItem, int preco) {
        if (jogador.getMoedas() < preco) {
            labelStatus.setText("Moedas insuficientes! Necessário: " + preco);
            return;
        }
 
        //Desconta moedas e salva no banco
        jogador.setMoedas(jogador.getMoedas() - preco);
        dao.atualizar(jogador);
        SessaoJogador.setJogador(jogador);
 
        //Adiciona ao inventário da sessão
        SessaoJogador.adicionarItem(nomeItem);
 
        atualizarMoedas();
        labelStatus.setText("'" + nomeItem + "' adicionado ao inventário!");
    }
 
    @FXML public void comprarMandado()    { comprar("Mandado de Busca", 50); }
    @FXML public void comprarInformante() { comprar("Informante", 80); }
    @FXML public void comprarForense()    { comprar("Análise Forense", 100); }
    @FXML public void voltarGameplay()    { SceneManager.navegar("gameplay"); }
    @FXML public void voltarMenu()        { SceneManager.navegar("menu"); }
}