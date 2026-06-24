package com.crimevariavel.controller;

import com.crimevariavel.dao.JogadorDAO;
import com.crimevariavel.model.Jogador;
import com.crimevariavel.util.SceneManager;
import com.crimevariavel.util.SessaoJogador;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class MenuController {

    @FXML private TextField campoNome;
    @FXML private Label labelErro;

    @FXML
    public void jogar() {
        String nome = campoNome.getText().trim();
        if (nome.isEmpty()) {
            labelErro.setText("Digite seu nome para jogar!");
            return;
        }
        JogadorDAO dao = new JogadorDAO();
        Jogador jogador = dao.buscarPorNome(nome);
        if (jogador == null) {
            jogador = new Jogador();
            jogador.setNome(nome);
            dao.salvar(jogador);
        }
        SessaoJogador.setJogador(jogador);
        SceneManager.navegar("gameplay");
    }

    @FXML public void abrirRanking()  { SceneManager.navegar("ranking"); }
    @FXML public void abrirCreditos() { SceneManager.navegar("creditos"); }
    @FXML public void encerrar()      { Platform.exit(); }
}
