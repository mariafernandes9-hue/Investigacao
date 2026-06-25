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
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
 
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
 
public class GameplayController {
 
    // ── Topo ──────────────────────────────────────────────────────────
    @FXML private Label labelJogador;
    @FXML private Label labelMoedas;
    @FXML private Label labelCasos;
    @FXML private Label labelDificuldade;
    @FXML private Label labelStatus;
 
    // ── Centro ────────────────────────────────────────────────────────
    @FXML private StackPane stackCentro;
    @FXML private ImageView imagemCena;
    @FXML private VBox overlayEscuro;
 
    // Mapa
    @FXML private VBox painelMapa;
    @FXML private HBox painelSuspeitos;
 
    // Cena do local
    @FXML private VBox painelCena;
    @FXML private Label labelLocalAtual;
    @FXML private Label labelTempo;
    @FXML private FlowPane areaCartoes;
 
    // Gaveta
    @FXML private HBox gavetaPistas;
    @FXML private VBox listaPistas;
 
    // ── Estado ────────────────────────────────────────────────────────
    private Caso casoAtual;
    private Jogador jogador;
    private JogadorDAO jogadorDAO;
    private List<String> upgradesAtivos;
 
    // Pistas já coletadas (para o caderno)
    private final List<String> pistasCaderno = new ArrayList<>();
 
    // Mapeamento local → nome do arquivo de imagem
    private static final Map<String, String> IMAGENS_LOCAIS = Map.of(
        "Recepção",   "recepao.png",
        "Restaurante","restaurante.png",
        "Quarto 201", "quarto201.png",
        "Jardim",     "jardim.png",
        "Garagem",    "garagem.png"
    );
    private static final String IMG_MAPA = "mapa_hotel.jng";
 
    // ── Inicialização ─────────────────────────────────────────────────
    @FXML
    public void initialize() {
        jogador    = SessaoJogador.getJogador();
        jogadorDAO = new JogadorDAO();
        upgradesAtivos = new UpgradeDAO().listarPorJogador(jogador.getId());
 
        atualizarTopo();
 
        if (SessaoJogador.getCasoAtual() != null) {
            casoAtual = SessaoJogador.getCasoAtual();
            setStatus("Caso retomado. Escolha um local para investigar.");
        } else {
            iniciarNovoCaso();
        }
 
        carregarImagem(imagemCena, IMG_MAPA);
        construirSuspeitos();
        mostrarMapa();
 
        // Faz a imagem preencher o centro dinamicamente
        imagemCena.fitWidthProperty().bind(stackCentro.widthProperty());
        imagemCena.fitHeightProperty().bind(stackCentro.heightProperty());
        overlayEscuro.prefWidthProperty().bind(stackCentro.widthProperty());
        overlayEscuro.prefHeightProperty().bind(stackCentro.heightProperty());
    }
 
    // ── Topo ──────────────────────────────────────────────────────────
    private void atualizarTopo() {
        labelJogador.setText("Detetive: " + jogador.getNome());
        labelMoedas .setText("💰 " + jogador.getMoedas());
        labelCasos  .setText("Casos: " + jogador.getCasosResolvidos());
        if (casoAtual != null) {
            String dif = casoAtual.getDificuldade().toUpperCase();
            String cor = switch (casoAtual.getDificuldade()) {
                case "facil"  -> "#22aa22";
                case "medio"  -> "#c8a030";
                case "dificil"-> "#cc2222";
                default       -> "#f0dfa8";
            };
            labelDificuldade.setText("[" + dif + "]");
            labelDificuldade.setStyle(
                "-fx-font-family:'Courier New'; -fx-font-size:12px; " +
                "-fx-font-weight:bold; -fx-text-fill:" + cor + ";");
        }
    }
 
    private void setStatus(String msg) {
        labelStatus.setText("  ▸ " + msg);
    }
 
    // ── Caso ──────────────────────────────────────────────────────────
    private void iniciarNovoCaso() {
        int casos = jogador.getCasosResolvidos();
        String dif = casos < 3 ? "facil" : casos < 7 ? "medio" : "dificil";
        casoAtual = GeradorCaso.gerar(dif);
        SessaoJogador.setCasoAtual(casoAtual);
        pistasCaderno.clear();
        atualizarTopo();
        setStatus("Novo caso gerado! Explore os locais do hotel.");
 
        // Upgrade: Rede de Contatos — 1 pista grátis
        if (upgradesAtivos.contains("Rede de Contatos")) {
            casoAtual.getPistas().stream().findFirst().ifPresent(p -> {
                adicionarAoCaderno("★ [REDE DE CONTATOS] " + p.getDescricao(), p.getLocal());
                setStatus("Rede de Contatos: 1 pista já revelada no caderno!");
            });
        }
    }
 
    // ── Mapa ──────────────────────────────────────────────────────────
    private void mostrarMapa() {
        carregarImagem(imagemCena, IMG_MAPA);
        painelMapa.setVisible(true);
        painelCena.setVisible(false);
        gavetaPistas.setVisible(false);
        overlayEscuro.setStyle("-fx-background-color:rgba(0,0,0,0.45);");
        construirSuspeitos(); // garante que os botões estão sempre presentes
    }
 
    private void construirSuspeitos() {
        painelSuspeitos.getChildren().clear();
        if (casoAtual == null) return;
        for (Suspeito s : casoAtual.getSuspeitos()) {
            Button btn = new Button("👤 " + s.getNome());
            btn.getStyleClass().add("card-suspeito");
            btn.setOnAction(e -> interrogar(s));
            btn.setWrapText(true);
            painelSuspeitos.getChildren().add(btn);
        }
    }
 
    // ── Entrar num local ──────────────────────────────────────────────
    @FXML
    private void entrarLocal(javafx.event.ActionEvent e) {
        Button btn = (Button) e.getSource();
        String local = (String) btn.getUserData();
        abrirCena(local);
    }
 
    private void abrirCena(String local) {
        // Imagem de fundo do local
        String arquivo = IMAGENS_LOCAIS.getOrDefault(local, IMG_MAPA);
        carregarImagem(imagemCena, arquivo);
        overlayEscuro.setStyle("-fx-background-color:rgba(0,0,0,0.55);");
 
        labelLocalAtual.setText("📍 " + local.toUpperCase());
        labelTempo.setText(casoAtual.getLinhaDoTempo());
 
        // Pistas do local como cards
        areaCartoes.getChildren().clear();
        List<Pista> pistasDoLocal = casoAtual.getPistas().stream()
                .filter(p -> p.getLocal().equals(local)).toList();
 
        if (pistasDoLocal.isEmpty()) {
            Label vazio = new Label("Nenhuma evidência encontrada neste local.");
            vazio.setStyle("-fx-font-family:'Courier New'; -fx-text-fill:#c8a030; -fx-font-size:13px;");
            areaCartoes.getChildren().add(vazio);
        } else {
            for (Pista p : pistasDoLocal) {
                areaCartoes.getChildren().add(criarCardPista(p));
            }
        }
 
        painelMapa.setVisible(false);
        painelCena.setVisible(true);
 
        // Ganha moedas por investigar
        jogador.setMoedas(jogador.getMoedas() + 10);
        salvarMoedas();
        setStatus("Investigando " + local + ". +10 moedas.");
 
        // Upgrade: Memória Fotográfica
        if (upgradesAtivos.contains("Memória Fotográfica")) {
            long verdadeiras = pistasDoLocal.stream()
                    .filter(p -> p.getTipo() == Pista.Tipo.VERDADEIRA).count();
            if (verdadeiras >= 2) {
                setStatus("◆ [MEM. FOTOGRÁFICA] " + verdadeiras
                        + " pistas neste local se confirmam entre si!");
            }
        }
    }
 
    // Card visual de pista (papel manila com texto)
    private VBox criarCardPista(Pista p) {
        VBox card = new VBox(6);
        card.getStyleClass().add("card-pista");
 
        // Marcador de tipo (visível só com Faro Apurado)
        if (upgradesAtivos.contains("Faro Apurado")) {
            String marcador = switch (p.getTipo()) {
                case VERDADEIRA -> "★ VERDADEIRA";
                case FALSA      -> "✗ FALSA";
                case INDIRETA   -> "◈ INDIRETA";
            };
            String cor = switch (p.getTipo()) {
                case VERDADEIRA -> "#006600";
                case FALSA      -> "#8b0000";
                case INDIRETA   -> "#5a4020";
            };
            Label tipo = new Label(marcador);
            tipo.setStyle("-fx-font-family:'Courier New'; -fx-font-size:9px; " +
                          "-fx-font-weight:bold; -fx-text-fill:" + cor + ";");
            card.getChildren().add(tipo);
        }
 
        // Texto da pista
        Label texto = new Label(p.getDescricao());
        texto.setStyle("-fx-font-family:'Courier New'; -fx-font-size:11px; -fx-text-fill:#1a0a00;");
        texto.setWrapText(true);
        texto.setMaxWidth(220);
        card.getChildren().add(texto);
 
        // Botão "Coletar"
        Button coletar = new Button("+ Coletar pista");
        coletar.setStyle(
            "-fx-background-color:transparent; -fx-text-fill:#8b0000; " +
            "-fx-font-family:'Courier New'; -fx-font-size:10px; " +
            "-fx-cursor:hand; -fx-underline:true; -fx-padding:4 0 0 0;");
        coletar.setOnAction(e -> {
            String prefixo = upgradesAtivos.contains("Faro Apurado")
                    ? "[" + p.getTipo().name() + "] " : "";
            adicionarAoCaderno(prefixo + p.getDescricao(), p.getLocal());
            coletar.setText("✓ Coletada");
            coletar.setDisable(true);
            setStatus("Pista coletada! Abra o caderno para revisar.");
            jogador.setMoedas(jogador.getMoedas() + 5);
            salvarMoedas();
        });
        card.getChildren().add(coletar);
 
        return card;
    }
 
    @FXML
    private void voltarMapa() {
        mostrarMapa();
        setStatus("Escolha outro local ou interrogue um suspeito.");
    }
 
    // ── Gaveta de pistas ──────────────────────────────────────────────
    @FXML
    private void toggleGaveta() {
        boolean aberta = gavetaPistas.isVisible();
        if (!aberta) atualizarGaveta();
        gavetaPistas.setVisible(!aberta);
    }
 
    @FXML
    private void fecharGaveta() {
        gavetaPistas.setVisible(false);
    }
 
    private void adicionarAoCaderno(String texto, String local) {
        String entrada = "[" + local + "] " + texto;
        if (!pistasCaderno.contains(entrada)) {
            pistasCaderno.add(entrada);
        }
    }
 
    private void atualizarGaveta() {
        listaPistas.getChildren().clear();
 
        if (pistasCaderno.isEmpty()) {
            Label vazio = new Label("Nenhuma pista coletada ainda.\nInvestigue os locais do hotel.");
            vazio.setStyle("-fx-font-family:'Courier New'; -fx-font-size:11px; " +
                           "-fx-text-fill:#5a4020; -fx-padding:16;");
            vazio.setWrapText(true);
            listaPistas.getChildren().add(vazio);
            return;
        }
 
        for (String pista : pistasCaderno) {
            VBox item = new VBox(2);
            item.getStyleClass().add("item-gaveta");
 
            Label txt = new Label(pista);
            txt.setStyle("-fx-font-family:'Courier New'; -fx-font-size:11px; -fx-text-fill:#f0dfa8;");
            txt.setWrapText(true);
            txt.setMaxWidth(300);
 
            item.getChildren().add(txt);
            listaPistas.getChildren().add(item);
        }
 
        // Linha do tempo no final
        Label sep = new Label("─────────────────────────────");
        sep.setStyle("-fx-text-fill:#3a2a10; -fx-padding:6 16 0 16;");
        Label lt = new Label("⏱ " + casoAtual.getLinhaDoTempo());
        lt.setStyle("-fx-font-family:'Courier New'; -fx-font-size:10px; " +
                    "-fx-text-fill:#8b6914; -fx-font-style:italic; -fx-padding:0 16 10 16;");
        lt.setWrapText(true);
        lt.setMaxWidth(300);
        listaPistas.getChildren().addAll(sep, lt);
    }
 
    // ── Interrogatório ────────────────────────────────────────────────
    private void interrogar(Suspeito s) {
        // Fechar cena se estiver aberta, voltar ao mapa
        mostrarMapa();
        gavetaPistas.setVisible(true);
        atualizarGaveta();
 
        StringBuilder registro = new StringBuilder();
        registro.append("[ INTERROGATÓRIO — ").append(s.getNome().toUpperCase()).append(" ]\n");
        registro.append("Personalidade: ").append(s.getPersonalidade()).append("\n");
 
        // Upgrade: Olho Clínico
        if (upgradesAtivos.contains("Olho Clínico")) {
            String tipoAlibi = s.isCulpado() ? "⚠ FALSO"
                    : s.getAlibi().contains("Verificável") ? "✓ VERDADEIRO" : "~ PARCIAL";
            registro.append("Álibi [").append(tipoAlibi).append("]: ");
        } else {
            registro.append("Álibi: ");
        }
        registro.append(s.getAlibi()).append("\n");
 
        // Upgrade: Intimidação
        if (upgradesAtivos.contains("Intimidação")) {
            registro.append("★ [INTIMIDAÇÃO] ");
            registro.append(s.isCulpado()
                ? s.getNome() + " demonstrou nervosismo ao mencionar o horário do crime.\n"
                : s.getNome() + " não soube detalhar onde estava com precisão.\n");
        }
 
        adicionarAoCaderno(registro.toString(), "Interrogatório");
        atualizarGaveta();
 
        jogador.setMoedas(jogador.getMoedas() + 5);
        salvarMoedas();
        setStatus("Interrogatório de " + s.getNome() + " registrado no caderno. +5 moedas.");
    }
 
    // ── Focar suspeitos (atalho do rodapé) ───────────────────────────
    @FXML
    private void focarSuspeitos() {
        mostrarMapa();
        painelSuspeitos.setStyle("-fx-background-color: rgba(139,0,0,0.25); -fx-background-radius: 6;");
        javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.millis(600));
        pause.setOnFinished(e -> painelSuspeitos.setStyle(""));
        pause.play();
        setStatus("Clique no nome de um suspeito para interrogar.");
    }
 
    // ── Itens consumíveis do inventário ───────────────────────────────
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
        StringBuilder txt = new StringBuilder("[ MANDADO DE BUSCA — " + local + " ]\n");
        casoAtual.getPistas().stream()
                .filter(p -> p.getLocal().equals(local))
                .forEach(p -> txt.append("• ").append(p.getDescricao()).append("\n"));
        adicionarAoCaderno(txt.toString(), local);
        setStatus("Mandado de Busca revelou todas as pistas de: " + local);
        if (gavetaPistas.isVisible()) atualizarGaveta();
    }
 
    private void usarInformante() {
        Suspeito inocente = casoAtual.getSuspeitos().stream()
                .filter(s -> !s.isCulpado()).findFirst().orElse(null);
        if (inocente == null) return;
        adicionarAoCaderno("[ INFORMANTE ] " + inocente.getNome() + " é INOCENTE.", "Informante");
        setStatus("Informante confirmou: " + inocente.getNome() + " está fora de suspeita.");
        if (gavetaPistas.isVisible()) atualizarGaveta();
    }
 
    private void usarForense() {
        casoAtual.getPistas().stream()
                .filter(p -> p.getTipo() == Pista.Tipo.VERDADEIRA)
                .findFirst()
                .ifPresent(p -> {
                    adicionarAoCaderno("[ FORENSE ] Evidência confirmada: " + p.getDescricao(), p.getLocal());
                    setStatus("Análise Forense confirmou uma evidência real.");
                    if (gavetaPistas.isVisible()) atualizarGaveta();
                });
    }
 
    // ── Imagem ────────────────────────────────────────────────────────
    private void carregarImagem(ImageView iv, String arquivo) {
        try {
            String path = "/com/crimevariavel/imagens/" + arquivo;
            var stream = getClass().getResourceAsStream(path);
            if (stream != null) {
                iv.setImage(new Image(stream));
            } else {
                // Fallback: fundo escuro via CSS se imagem não encontrada
                iv.setImage(null);
            }
        } catch (Exception e) {
            iv.setImage(null);
        }
    }
 
    // ── Moedas ────────────────────────────────────────────────────────
    private void salvarMoedas() {
        jogadorDAO.atualizar(jogador);
        SessaoJogador.setJogador(jogador);
        labelMoedas.setText("💰 " + jogador.getMoedas());
    }
 
    // ── Inventário (Stage separado) ───────────────────────────────────
    @FXML
    public void abrirInventario() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/crimevariavel/inventario.fxml"));
            Stage inv = new Stage();
            inv.setTitle("Inventário");
            inv.setScene(new Scene(loader.load()));
            inv.initModality(Modality.APPLICATION_MODAL);
            inv.setOnHidden(e -> {
                String usado = SessaoJogador.getItemUsado();
                if (usado != null) {
                    aplicarEfeitoItem(usado);
                    SessaoJogador.limparItemUsado();
                }
            });
            inv.show();
        } catch (Exception e) { e.printStackTrace(); }
    }
 
    // ── Navegação ─────────────────────────────────────────────────────
    @FXML public void abrirBoss()     { SceneManager.navegar("boss"); }
    @FXML public void acusar()        { SceneManager.navegar("acusacao"); }
    @FXML public void abrirUpgrades() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/crimevariavel/upgrades.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Upgrades");
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) { e.printStackTrace(); }
    }
 
    @FXML public void abrirLoja() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/crimevariavel/loja.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Loja");
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) { e.printStackTrace(); }
    }
    @FXML public void voltarMenu()    { SceneManager.navegar("menu"); }
}