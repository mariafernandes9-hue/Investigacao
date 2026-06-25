package com.crimevariavel.controller;

import com.crimevariavel.dao.JogadorDAO;
import com.crimevariavel.dao.UpgradeDAO;
import com.crimevariavel.model.Jogador;
import com.crimevariavel.util.SceneManager;
import com.crimevariavel.util.SessaoJogador;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import java.util.List;
 
public class UpgradesController {
 
    @FXML private Label labelPontos;
    @FXML private Label labelStatus;
 
    private Jogador jogador;
    private UpgradeDAO upgradeDAO;
    private JogadorDAO jogadorDAO;
    private List<String> upgradesComprados;
 
    @FXML
    public void initialize() {
        jogador = SessaoJogador.getJogador();
        upgradeDAO = new UpgradeDAO();
        jogadorDAO = new JogadorDAO();
        upgradesComprados = upgradeDAO.listarPorJogador(jogador.getId());
        atualizarLabel();
    }
 
    private void atualizarLabel() {
        labelPontos.setText("Pontos disponíveis: " + jogador.getPontosUpgrade());
    }
 
    private void comprar(String tipo, int custo) {
        if (upgradesComprados.contains(tipo)) {
            labelStatus.setText("Você já possui: " + tipo);
            return;
        }
        if (jogador.getPontosUpgrade() < custo) {
            labelStatus.setText("Pontos insuficientes! Necessário: " + custo);
            return;
        }
        jogador.setPontosUpgrade(jogador.getPontosUpgrade() - custo);
        jogadorDAO.atualizar(jogador);
        upgradeDAO.salvar(jogador.getId(), tipo);
        upgradesComprados.add(tipo);
        atualizarLabel();
        labelStatus.setText("'" + tipo + "' adquirido com sucesso!");
    }
 
    @FXML public void comprarFaroApurado()      { comprar("Faro Apurado", 3); }
    @FXML public void comprarIntimidacao()       { comprar("Intimidação", 3); }
    @FXML public void comprarRedeContatos()      { comprar("Rede de Contatos", 4); }
    @FXML public void comprarMemoriaFotografica(){ comprar("Memória Fotográfica", 5); }
    @FXML public void comprarOlhoClinico()       { comprar("Olho Clínico", 5); }
    
    @FXML public void fecharJanela() { ((Stage) labelStatus.getScene().getWindow()).close(); }
    @FXML public void voltarMenu()               { SceneManager.navegar("menu"); }
}
 