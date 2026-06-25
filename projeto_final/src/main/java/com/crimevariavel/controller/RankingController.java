package com.crimevariavel.controller;

import com.crimevariavel.dao.JogadorDAO;
import com.crimevariavel.model.Jogador;
import com.crimevariavel.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import java.util.List;
import javafx.scene.control.Label;

public class RankingController {

    @FXML
    private TextArea areaRanking;
    
    @FXML 
    private Label labelStatus;


    @FXML
    public void initialize() {
        labelStatus.setText("");
        JogadorDAO dao = new JogadorDAO();
        List<Jogador> lista = dao.listarRanking();

        if (lista.isEmpty()) {
            areaRanking.setText("Nenhum detetive cadastrado ainda.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-4s %-22s %-10s %-10s%n",
                "POS", "NOME", "VITÓRIAS", "MOEDAS"));
        sb.append("─".repeat(50)).append("\n");

        for (int i = 0; i < lista.size(); i++) {
            Jogador j = lista.get(i);
            //casos_resolvidos só é incrementado em vitórias (AcusacaoController)
            sb.append(String.format("%-4s %-22s %-10s %-10s%n",
                    (i + 1) + "º",
                    j.getNome(),
                    j.getCasosResolvidos(),
                    j.getMoedas()));
        }

        areaRanking.setText(sb.toString());
    }
    
    @FXML
    public void exportarCSV() {
    JogadorDAO dao = new JogadorDAO();
    List<Jogador> lista = dao.listarRanking();
    StringBuilder csv = new StringBuilder("posicao,nome,casos_resolvidos,moedas\n");
    for (int i = 0; i < lista.size(); i++) {
        Jogador j = lista.get(i);
        csv.append(String.format("%d,%s,%d,%d%n",
                i + 1, j.getNome(), j.getCasosResolvidos(), j.getMoedas()));
    }
    String caminho = System.getProperty("user.home") + "/ranking_crime_variavel.csv";
    try {
        java.nio.file.Files.writeString(java.nio.file.Path.of(caminho), csv.toString());
        labelStatus.setText("Exportado: " + caminho);
    } catch (Exception e) {
        labelStatus.setText("Erro: " + e.getMessage());
    }
}

    @FXML public void voltarMenu() { SceneManager.navegar("menu"); }
}
