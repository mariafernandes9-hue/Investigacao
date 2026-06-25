package com.crimevariavel.controller;

import com.crimevariavel.model.Jogador;
import com.crimevariavel.util.SceneManager;
import com.crimevariavel.util.SessaoJogador;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class BossController {

    @FXML 
    private Label labelDescricao;
    
    @FXML 
    private Label labelStatus;
    
    @FXML 
    private Button botaoIniciar;

    @FXML
    public void initialize() {
        Jogador j = SessaoJogador.getJogador();
        int casos = j.getCasosResolvidos();
        int necessario = 5;

        if (casos >= necessario) {
            labelDescricao.setText("O Grande Crime foi descoberto. Um assassinato no coração do hotel.\n" +
                "Cinco suspeitos. Dezenas de pistas — a maioria falsa.\nVocê está pronto?");
            botaoIniciar.setDisable(false);
            labelStatus.setText("Desbloqueado! Casos resolvidos: " + casos);
        } else {
            labelDescricao.setText("Você ainda não está pronto para O Grande Crime.\n" +
                "Resolva mais " + (necessario - casos) + " caso(s) para desbloquear.");
            botaoIniciar.setDisable(true);
            labelStatus.setText("Necessário: " + necessario + " casos | Seus casos: " + casos);
        }
    }

    @FXML
    public void iniciarBossFinal() {
        // Gera um caso no modo difícil como boss final
        com.crimevariavel.util.SessaoJogador.setCasoAtual(
            com.crimevariavel.util.GeradorCaso.gerar("dificil")
        );
        SessaoJogador.setMoedasRun(0);
        SceneManager.navegar("gameplay");
    }

    @FXML public void voltarMenu() { SceneManager.navegar("menu"); }
}
