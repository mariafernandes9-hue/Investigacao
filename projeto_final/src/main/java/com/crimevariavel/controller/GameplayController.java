package com.crimevariavel.controller;

import com.crimevariavel.dao.JogadorDAO;
import com.crimevariavel.dao.UpgradeDAO;
import com.crimevariavel.model.Caso;
import com.crimevariavel.model.Jogador;
import com.crimevariavel.model.Pista;
import com.crimevariavel.model.Suspeito;
import com.crimevariavel.util.GeradorCaso;
import com.crimevariavel.util.SceneManager;
import com.crimevariavel.util.SessaoJogador;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class GameplayController {

    @FXML private Label labelJogador;
    @FXML private Label labelMoedas;
    @FXML private Label labelCasos;
    @FXML private Label labelStatus;
    @FXML private TextArea areaPistas;
    @FXML private VBox painelSuspeitos;
    @FXML private VBox painelLocais;

    private Caso casoAtual;
    private Jogador jogador;
    private JogadorDAO jogadorDAO;

    //guarda upgrades ativos do jogador
    private List<String> upgradesAtivos;

    @FXML
    public void initialize() {
        jogador = SessaoJogador.getJogador();
        jogadorDAO = new JogadorDAO();

        //carrega upgrades permanentes do banco
        upgradesAtivos = new UpgradeDAO().listarPorJogador(jogador.getId());

        labelJogador.setText("Detetive: " + jogador.getNome());
        labelCasos.setText("Casos: " + jogador.getCasosResolvidos());
        atualizarMoedas();

        if (SessaoJogador.getCasoAtual() != null) {
            casoAtual = SessaoJogador.getCasoAtual();
            configurarBotoes();
            labelStatus.setText("Caso retomado. Continue investigando!");
        } else {
            iniciarNovoCaso();
        }
    }

    @FXML
    public void abrirBoss() {
        SceneManager.navegar("boss");
    }

    private void atualizarMoedas() {
        labelMoedas.setText("Moedas: " + jogador.getMoedas());
    }

    private void salvarMoedas() {
        jogadorDAO.atualizar(jogador);
        SessaoJogador.setJogador(jogador);
        atualizarMoedas();
    }

    private void iniciarNovoCaso() {
        String dificuldade;
        int casos = jogador.getCasosResolvidos();
        if (casos < 3)      dificuldade = "facil";
        else if (casos < 7) dificuldade = "medio";
        else                dificuldade = "dificil";

        casoAtual = GeradorCaso.gerar(dificuldade);
        SessaoJogador.setCasoAtual(casoAtual);
        areaPistas.clear();
        labelStatus.setText("Caso gerado! Dificuldade: " + dificuldade.toUpperCase());
        configurarBotoes();

        //UPGRADE 1: Rede de Contatos — revela 1 pista automaticamente
        if (upgradesAtivos.contains("Rede de Contatos")) {
            casoAtual.getPistas().stream().findFirst().ifPresent(p -> {
                areaPistas.appendText("[ REDE DE CONTATOS — pista inicial revelada ]\n");
                areaPistas.appendText("• " + p.getDescricao() + "\n");
                areaPistas.appendText("─────────────────────────────\n");
            });
            labelStatus.setText("Rede de Contatos: 1 pista revelada automaticamente!");
        }
    }

    private void configurarBotoes() {
        painelSuspeitos.getChildren().clear();
        Label ts = new Label("[ SUSPEITOS ]");
        ts.setStyle("-fx-font-family: 'Courier New'; -fx-text-fill: #c8a030; -fx-font-weight: bold; -fx-font-size: 11px;");
        painelSuspeitos.getChildren().add(ts);

        for (Suspeito s : casoAtual.getSuspeitos()) {
            Button btn = new Button(s.getNome());
            btn.setPrefWidth(175);
            btn.setWrapText(true);
            btn.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 11px;");
            btn.setOnAction(e -> interrogar(s));
            painelSuspeitos.getChildren().add(btn);
        }

        painelLocais.getChildren().clear();
        Label tl = new Label("[ LOCAIS ]");
        tl.setStyle("-fx-font-family: 'Courier New'; -fx-text-fill: #c8a030; -fx-font-weight: bold; -fx-font-size: 11px;");
        painelLocais.getChildren().add(tl);

        List<String> locais = casoAtual.getPistas().stream()
                .map(Pista::getLocal).distinct().toList();
        for (String local : locais) {
            Button btn = new Button(local);
            btn.setPrefWidth(175);
            btn.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 11px;");
            btn.setOnAction(e -> investigarLocal(local));
            painelLocais.getChildren().add(btn);
        }
    }

    private void interrogar(Suspeito s) {
        areaPistas.appendText("\n[ INTERROGATÓRIO — " + s.getNome().toUpperCase() + " ]\n");
        areaPistas.appendText("Personalidade: " + s.getPersonalidade() + "\n");

        //UPGRADE2: Olho Clínico — identifica tipo do álibi
        String alibi = s.getAlibi();
        if (upgradesAtivos.contains("Olho Clínico")) {
            String tipoAlibi;
            if (s.isCulpado()) {
                tipoAlibi = "⚠ ÁLIBI FALSO";
            } else if (alibi.contains("Verificável")) {
                tipoAlibi = "✓ ÁLIBI VERDADEIRO";
            } else {
                tipoAlibi = "~ ÁLIBI PARCIAL";
            }
            areaPistas.appendText("Álibi [" + tipoAlibi + "]: " + alibi + "\n");
        } else {
            areaPistas.appendText("Álibi: " + alibi + "\n");
        }

        //UPGRADE3: Intimidação — revela detalhe extra
        if (upgradesAtivos.contains("Intimidação")) {
            if (s.isCulpado()) {
                areaPistas.appendText("★ [INTIMIDAÇÃO] " + s.getNome()
                        + " demonstrou nervosismo ao mencionar o horário do crime.\n");
            } else {
                areaPistas.appendText("★ [INTIMIDAÇÃO] " + s.getNome()
                        + " não soube detalhar onde estava com precisão.\n");
            }
        }

        areaPistas.appendText("─────────────────────────────\n");
        jogador.setMoedas(jogador.getMoedas() + 5);
        salvarMoedas();
        labelStatus.setText("Interrogatório de " + s.getNome() + " registrado. +5 moedas.");
    }

    private void investigarLocal(String local) {
        areaPistas.appendText("\n[ LOCAL — " + local.toUpperCase() + " ]\n");
        List<Pista> pistasdoLocal = casoAtual.getPistas().stream()
                .filter(p -> p.getLocal().equals(local)).toList();

        if (pistasdoLocal.isEmpty()) {
            areaPistas.appendText("Nenhuma pista encontrada aqui.\n");
        } else {
            for (Pista p : pistasdoLocal) {
                //UPGRADE4: Faro Apurado — marca tipo da pista
                if (upgradesAtivos.contains("Faro Apurado")) {
                    String marcador = switch (p.getTipo()) {
                        case VERDADEIRA -> "★ [VERDADEIRA]";
                        case FALSA      -> "✗ [FALSA]";
                        case INDIRETA   -> "◈ [INDIRETA]";
                    };
                    areaPistas.appendText(marcador + " " + p.getDescricao() + "\n");
                } else {
                    areaPistas.appendText("• " + p.getDescricao() + "\n");
                }
            }

            //UPGRADE5: Memória Fotográfica — destaca pistas que se confirmam
            if (upgradesAtivos.contains("Memória Fotográfica")) {
                long verdadeiras = pistasdoLocal.stream()
                        .filter(p -> p.getTipo() == Pista.Tipo.VERDADEIRA).count();
                if (verdadeiras >= 2) {
                    areaPistas.appendText("◆ [MEM. FOTOGRÁFICA] "
                            + verdadeiras + " pistas neste local se confirmam entre si!\n");
                }
            }

            jogador.setMoedas(jogador.getMoedas() + 10);
            salvarMoedas();
            labelStatus.setText("Local investigado: " + local + ". +10 moedas.");
        }
        areaPistas.appendText("─────────────────────────────\n");
    }

    //aplicar os efeitos dos updates

    private void aplicarEfeitoItem(String item) {
        switch (item) {
            case "Mandado de Busca" -> usarMandado();
            case "Informante"       -> usarInformante();
            case "Análise Forense"  -> usarForense();
        }
    }

    private void usarMandado() {
        List<String> locais = casoAtual.getPistas().stream()
                .map(Pista::getLocal).distinct().toList();
        if (locais.isEmpty()) return;
        String local = locais.get((int)(Math.random() * locais.size()));
        areaPistas.appendText("\n[ MANDADO DE BUSCA — " + local.toUpperCase() + " ]\n");
        areaPistas.appendText("Acesso forçado ao local. Todas as evidências reveladas:\n");
        casoAtual.getPistas().stream()
                .filter(p -> p.getLocal().equals(local))
                .forEach(p -> areaPistas.appendText("• " + p.getDescricao() + "\n"));
        areaPistas.appendText("─────────────────────────────\n");
        labelStatus.setText("Mandado de Busca usado em: " + local);
    }

    private void usarInformante() {
        Suspeito inocente = casoAtual.getSuspeitos().stream()
                .filter(s -> !s.isCulpado())
                .findFirst().orElse(null);
        if (inocente == null) {
            areaPistas.appendText("\n[ INFORMANTE ] Nenhum inocente identificável no momento.\n");
            return;
        }
        areaPistas.appendText("\n[ INFORMANTE ]\n");
        areaPistas.appendText("Fonte confidencial confirma: " + inocente.getNome()
                + " é INOCENTE. Elimine-o(a) da lista de suspeitos.\n");
        areaPistas.appendText("─────────────────────────────\n");
        labelStatus.setText("Informante revelou: " + inocente.getNome() + " é inocente.");

        painelSuspeitos.getChildren().removeIf(node ->
                node instanceof Button &&
                ((Button) node).getText().equals(inocente.getNome()));
    }

    private void usarForense() {
        casoAtual.getPistas().stream()
                .filter(p -> p.getTipo() == Pista.Tipo.VERDADEIRA)
                .findFirst()
                .ifPresentOrElse(p -> {
                    areaPistas.appendText("\n[ ANÁLISE FORENSE ]\n");
                    areaPistas.appendText("Laudo confirma evidência verdadeira:\n");
                    areaPistas.appendText("★ " + p.getDescricao() + "\n");
                    areaPistas.appendText("─────────────────────────────\n");
                    labelStatus.setText("Análise Forense confirmou uma evidência real.");
                }, () -> {
                    areaPistas.appendText("\n[ ANÁLISE FORENSE ] Nenhuma evidência física disponível.\n");
                });
    }

    //abrir inventário como Stage separado

    @FXML
    public void abrirInventario() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/crimevariavel/inventario.fxml"));
            Stage inventarioStage = new Stage();
            inventarioStage.setTitle("Inventário");
            inventarioStage.setScene(new Scene(loader.load()));
            inventarioStage.initModality(Modality.APPLICATION_MODAL);

            inventarioStage.setOnHidden(e -> {
                String itemUsado = SessaoJogador.getItemUsado();
                if (itemUsado != null) {
                    aplicarEfeitoItem(itemUsado);
                    SessaoJogador.limparItemUsado();
                }
            });

            inventarioStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML public void acusar()        { SceneManager.navegar("acusacao"); }
    @FXML public void abrirUpgrades() { SceneManager.navegar("upgrades"); }
    @FXML public void abrirLoja()     { SceneManager.navegar("loja"); }
    @FXML public void voltarMenu()    { SceneManager.navegar("menu"); }
}
