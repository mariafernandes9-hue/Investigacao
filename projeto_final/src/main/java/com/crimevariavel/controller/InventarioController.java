package com.crimevariavel.controller;

import com.crimevariavel.dao.UpgradeDAO;
import com.crimevariavel.model.Jogador;
import com.crimevariavel.util.SessaoJogador;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
 
import java.util.List;
import java.util.Map;
 
public class InventarioController {
 
    @FXML 
    private VBox areaItens;
    
    @FXML 
    private Label labelJogador;
 
    @FXML
    public void initialize() {
        Jogador j = SessaoJogador.getJogador();
        labelJogador.setText("Inventário de: " + j.getNome()
                + "  |  Moedas: " + j.getMoedas()
                + "  |  Pts upgrade: " + j.getPontosUpgrade());
 
        areaItens.getChildren().clear();
 
        //itens consumíveis 
        Label tituloItens = new Label("ITENS DISPONÍVEIS");
        tituloItens.setStyle("-fx-font-family: 'Courier New'; -fx-font-weight: bold; "
                + "-fx-text-fill: #5a0000; -fx-font-size: 13px;");
        areaItens.getChildren().add(tituloItens);
 
        Map<String, Integer> inventario = SessaoJogador.getInventario();
 
        if (inventario.isEmpty()) {
            Label vazio = new Label("Nenhum item no inventário. Visite a Loja!");
            vazio.setStyle("-fx-font-family: 'Courier New'; -fx-text-fill: #5a4020; -fx-font-size: 12px;");
            areaItens.getChildren().add(vazio);
        } else {
            for (Map.Entry<String, Integer> entry : inventario.entrySet()) {
                String item = entry.getKey();
                int qtd = entry.getValue();
 
                HBox linha = new HBox(15);
                linha.setStyle("-fx-padding: 5;");
 
                Label lblItem = new Label("• " + item + "  (x" + qtd + ")");
                lblItem.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 12px; "
                        + "-fx-text-fill: #2a1a05; -fx-pref-width: 220;");
 
                Label lblDesc = new Label(descricao(item));
                lblDesc.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 11px; "
                        + "-fx-text-fill: #5a4020; -fx-pref-width: 220;");
                lblDesc.setWrapText(true);
 
                Button btnUsar = new Button("USAR");
                btnUsar.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 11px;");
                btnUsar.setOnAction(e -> usarItem(item));
 
                linha.getChildren().addAll(lblItem, lblDesc, btnUsar);
                areaItens.getChildren().add(linha);
            }
        }
 
        //upgrades permanentes
        Label separador = new Label("─────────────────────────────────────");
        separador.setStyle("-fx-text-fill: #8b6914;");
        areaItens.getChildren().add(separador);
 
        Label tituloUpgrades = new Label("UPGRADES PERMANENTES");
        tituloUpgrades.setStyle("-fx-font-family: 'Courier New'; -fx-font-weight: bold; "
                + "-fx-text-fill: #5a0000; -fx-font-size: 13px;");
        areaItens.getChildren().add(tituloUpgrades);
 
        UpgradeDAO upgradeDAO = new UpgradeDAO();
        List<String> upgrades = upgradeDAO.listarPorJogador(j.getId());
 
        if (upgrades.isEmpty()) {
            Label semUpgrade = new Label("Nenhum upgrade adquirido ainda.");
            semUpgrade.setStyle("-fx-font-family: 'Courier New'; -fx-text-fill: #5a4020; -fx-font-size: 12px;");
            areaItens.getChildren().add(semUpgrade);
        } else {
            for (String u : upgrades) {
                Label lblUpgrade = new Label("✓  " + u);
                lblUpgrade.setStyle("-fx-font-family: 'Courier New'; -fx-text-fill: #2a1a05; -fx-font-size: 12px;");
                areaItens.getChildren().add(lblUpgrade);
            }
        }
    }
 
    private String descricao(String item) {
        return switch (item) {
            case "Mandado de Busca" -> "Revela todas as pistas de um local automaticamente.";
            case "Informante"       -> "Elimina um suspeito inocente da investigação.";
            case "Análise Forense"  -> "Confirma qual pista é verdadeira no caso.";
            default                 -> "";
        };
    }
 
    private void usarItem(String item) {
        if (!SessaoJogador.removerItem(item)) return;
 
        //Sinaliza para o GameplayController qual item foi usado
        SessaoJogador.setItemUsado(item);
 
        //Fecha o inventário - o GameplayController detecta e aplica o efeito
        fechar();
    }
 
    @FXML
    public void fechar() {
        ((Stage) areaItens.getScene().getWindow()).close();
    }
}
 