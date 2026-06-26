package com.crimevariavel.controller;


import com.crimevariavel.model.Caso;
import com.crimevariavel.model.Jogador;
import com.crimevariavel.util.GeradorCaso;
import com.crimevariavel.util.SceneManager;
import com.crimevariavel.util.SessaoJogador;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
 
public class BossController {
 
    @FXML private Label labelDescricao;
    @FXML private Label labelStatus;
    @FXML private Button btnIniciar;
 
    private static final int CASOS_NECESSARIOS = 5;
 
    @FXML
    public void initialize() {
        if (btnIniciar == null) return;
 
        Jogador j = SessaoJogador.getJogador();
        int casos = j != null ? j.getCasosResolvidos() : 0;
 
        if (casos >= CASOS_NECESSARIOS) {
            labelDescricao.setText(
                "Um assassinato no coração do Grand Hotel Eclipse.\n" +
                "Cinco suspeitos. Uma noite de tempestade. Nenhuma saída.\n\n" +
                "As pistas são escassas e os álibis — elaboradamente falsos.\n" +
                "Você não terá upgrades automáticos. Apenas o que conquistou.\n\n" +
                "Este é O Grande Crime. Resolva-o."
            );
            btnIniciar.setDisable(false);
            labelStatus.setText("✓ Desbloqueado — " + casos + " caso(s) resolvido(s)");
            labelStatus.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 11px; -fx-text-fill: #006600;");
        } else {
            int faltam = CASOS_NECESSARIOS - casos;
            labelDescricao.setText(
                "Você ainda não está pronto para O Grande Crime.\n\n" +
                "Apenas detetives experientes podem enfrentar este caso.\n" +
                "Resolva mais " + faltam + " caso(s) para desbloquear o acesso."
            );
            btnIniciar.setDisable(true);
            labelStatus.setText("✗ Bloqueado — " + casos + "/" + CASOS_NECESSARIOS + " casos resolvidos");
            labelStatus.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 11px; -fx-text-fill: #8b0000;");
        }
    }
 
    @FXML
    public void iniciarBossFinal() {
        // Gera caso no nível difícil e marca como boss na sessão
        Caso boss = GeradorCaso.gerar("dificil");
        SessaoJogador.setCasoAtual(boss);
        SessaoJogador.setModoBoss(true); // flag para gameplay exibir banner especial
        SceneManager.navegar("gameplay");
    }
 
    @FXML public void voltarMenu() { SceneManager.navegar("menu"); }
}