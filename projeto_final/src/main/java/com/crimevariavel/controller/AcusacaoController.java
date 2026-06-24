package com.crimevariavel.controller;
 
import com.crimevariavel.dao.JogadorDAO;
import com.crimevariavel.dao.PartidaDAO;
import com.crimevariavel.model.Caso;
import com.crimevariavel.model.Jogador;
import com.crimevariavel.model.Suspeito;
import com.crimevariavel.util.SceneManager;
import com.crimevariavel.util.SessaoJogador;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
 
public class AcusacaoController {
 
    @FXML private VBox painelSuspeitos;
    @FXML private Label labelResultado;
    @FXML private Button botaoProximo;
    @FXML private Button botaoMenu;
 
    private Caso caso;
 
    @FXML
    public void initialize() {
        caso = SessaoJogador.getCasoAtual();
        botaoProximo.setVisible(false);
        botaoMenu.setVisible(false);
 
        for (Suspeito s : caso.getSuspeitos()) {
            Button botao = new Button(s.getNome());
            botao.setPrefWidth(280);
            botao.setOnAction(e -> acusar(s));
            painelSuspeitos.getChildren().add(botao);
        }
    }
 
    private void acusar(Suspeito s) {
        Jogador jogador = SessaoJogador.getJogador();
        JogadorDAO dao = new JogadorDAO();
        PartidaDAO pdao = new PartidaDAO();
 
        if (s.isCulpado()) {
            int bonus = switch (caso.getDificuldade()) {
                case "dificil" -> 200;
                case "medio"   -> 150;
                default        -> 100;
            };
 
            jogador.setMoedas(jogador.getMoedas() + bonus);
            jogador.setPontosUpgrade(jogador.getPontosUpgrade() + 3);
            jogador.setCasosResolvidos(jogador.getCasosResolvidos() + 1);
            dao.atualizar(jogador);
            SessaoJogador.setJogador(jogador);
            pdao.salvar(jogador.getId(), "vitoria", bonus, caso.getDificuldade());
 
            labelResultado.setStyle("-fx-font-family: 'Courier New'; -fx-text-fill: #006600; -fx-font-size: 14px;");
            labelResultado.setText(
                "CORRETO! " + s.getNome() + " era o(a) culpado(a)!\n" +
                "Motivação: " + caso.getMotivacaoVerdadeira() + "\n\n" +
                "Bônus de vitória: +" + bonus + " moedas  |  +3 pontos de upgrade"
            );
        } else {
            pdao.salvar(jogador.getId(), "derrota", 0, caso.getDificuldade());
            labelResultado.setStyle("-fx-font-family: 'Courier New'; -fx-text-fill: #8b0000; -fx-font-size: 14px;");
            labelResultado.setText(
                "ERRADO! " + s.getNome() + " era inocente!\n" +
                "O verdadeiro culpado era: " + caso.getCulpado().getNome() + "\n" +
                "Motivação: " + caso.getMotivacaoVerdadeira()
            );
        }
 
        // Limpa o caso da sessão — run encerrada
        SessaoJogador.setCasoAtual(null);
 
        painelSuspeitos.setDisable(true);
        botaoProximo.setVisible(true);
        botaoMenu.setVisible(true);
    }
 
    @FXML public void proximoCaso() { SceneManager.navegar("gameplay"); }
    @FXML public void voltarMenu()  { SceneManager.navegar("menu"); }
}
