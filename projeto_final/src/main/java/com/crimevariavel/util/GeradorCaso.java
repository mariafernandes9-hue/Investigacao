package com.crimevariavel.util;

import com.crimevariavel.model.Caso;
import com.crimevariavel.model.Pista;
import com.crimevariavel.model.Suspeito;

import java.util.*;

/**
 * Geração procedural de casos.
 * A dificuldade afeta: quantidade de pistas, qualidade dos álibis,
 * clareza da linha do tempo e quão convincentes são as pistas falsas.
 */
public class GeradorCaso {

    private static final Random random = new Random();

    private static final String[][] PERSONAGENS = {
        {"Dr. Renato Campos",   "Frio e calculista. Responde perguntas com outra pergunta."},
        {"Sofia Monteiro",      "Ansiosa e nervosa. Fala demais quando pressionada."},
        {"Marcos Teixeira",     "Charmoso e mentiroso. Sorriso nunca sai do rosto."},
        {"Dona Lurdes",         "Discreta e observadora. Sabe mais do que aparenta."},
        {"Pierre Laurent",      "Arrogante e impaciente. Trata todos como suspeitos."},
        {"Ana Beatriz Souza",   "Simpática mas evasiva. Muda de assunto com habilidade."},
        {"Roberto Faria",       "Agressivo quando pressionado. Histórico de brigas."},
        {"Isabela Nunes",       "Calma demais para a situação. Quase não demonstra emoção."}
    };

    private static final String[] MOTIVACOES = {
        "inveja de uma herança milionária",
        "vingança por uma humilhação pública",
        "dívidas impagáveis que a vítima conhecia",
        "um segredo comprometedor que precisava ser silenciado",
        "amor não correspondido que virou obsessão perigosa",
        "disputa acirrada por um contrato de negócios",
        "chantagem sofrida há meses pela vítima",
        "testamento adulterado que beneficiava apenas a vítima",
        "traição descoberta na véspera do crime",
        "identidade falsa prestes a ser revelada"
    };

    private static final String[] LOCAIS = {
        "Quarto 201", "Restaurante", "Recepção", "Jardim", "Garagem"
    };

    // Linhas do tempo por dificuldade
    private static final String[] TEMPO_FACIL = {
        "O crime ocorreu às 22h em ponto — câmeras confirmam o horário.",
        "A vítima foi encontrada às 21h30. O crime ocorreu entre 21h e 21h30.",
        "Registros do hotel indicam que o crime aconteceu às 23h, durante o turno da noite."
    };

    private static final String[] TEMPO_MEDIO = {
        "O crime ocorreu entre 21h e 23h. Testemunhas divergem sobre o horário exato.",
        "Estima-se que o crime aconteceu à meia-noite, mas nenhuma câmera cobria o local.",
        "A vítima foi vista pela última vez às 20h. O horário do crime é incerto."
    };

    private static final String[] TEMPO_DIFICIL = {
        "O horário do crime é desconhecido. A vítima pode ter sido envenenada horas antes.",
        "Dois hóspedes afirmam ter ouvido barulho em horários diferentes — impossível precisar.",
        "O laudo preliminar indica uma janela de 4 horas para o crime. Nada mais foi confirmado.",
        "O relógio da sala estava parado em 23h45 — pode ser coincidência, pode não ser."
    };

    // Templates de pistas verdadeiras — pool grande para variar entre casos
    private static String[] buildPistasVerdadeiras(Suspeito culpado, String local, String motivacao) {
        return new String[]{
            culpado.getNome() + " foi visto(a) no " + local + " pouco antes do crime.",
            "Uma digital de " + culpado.getNome() + " foi encontrada na cena do crime.",
            culpado.getNome() + " tinha motivo claro: " + motivacao + ".",
            "O álibi de " + culpado.getNome() + " não se sustenta. Ninguém confirma sua versão.",
            "Uma testemunha ouviu " + culpado.getNome() + " ameaçando a vítima horas antes.",
            "Câmera registra " + culpado.getNome() + " saindo do " + local + " às pressas.",
            "Um objeto pessoal de " + culpado.getNome() + " foi encontrado próximo à vítima.",
            "Registros telefônicos mostram ligação de " + culpado.getNome() + " para a vítima minutos antes.",
            culpado.getNome() + " apresentou nervosismo extremo ao ser questionado(a) sobre o horário.",
            "Funcionário do hotel viu " + culpado.getNome() + " no corredor próximo à cena."
        };
    }

    // Pistas falsas FÁCEIS — obviamente não ligadas ao crime
    private static String[] buildPistasFalsasFaceis(List<Suspeito> inocentes, String[] locais) {
        List<String> pool = new ArrayList<>();
        for (Suspeito s : inocentes) {
            String local = locais[random.nextInt(locais.length)];
            pool.add(s.getNome() + " estava nervoso(a) e evitou contato visual.");
            pool.add("Uma foto mostra " + s.getNome() + " perto do local — mas em outro horário.");
            pool.add(s.getNome() + " conhecia a vítima há anos, mas não tinha motivo aparente.");
            pool.add("Encontraram um item de " + s.getNome() + " na cena, possivelmente perdido antes.");
            pool.add(s.getNome() + " tinha uma discussão antiga com a vítima, já resolvida.");
        }
        Collections.shuffle(pool);
        return pool.toArray(new String[0]);
    }

    // Pistas falsas MÉDIAS — mais ambíguas
    private static String[] buildPistasFalsasMedias(List<Suspeito> inocentes, String[] locais) {
        List<String> pool = new ArrayList<>();
        for (Suspeito s : inocentes) {
            String local = locais[random.nextInt(locais.length)];
            pool.add(s.getNome() + " foi visto(a) saindo do " + local + " no horário do crime — mas por motivo não confirmado.");
            pool.add("Um hóspede afirma ter ouvido a voz de " + s.getNome() + " próxima à cena. Não há confirmação.");
            pool.add("Registros indicam que " + s.getNome() + " tentou acessar o " + local + " no dia do crime.");
            pool.add(s.getNome() + " mentiu sobre onde estava — mas a mentira parece não ter relação com o crime.");
            pool.add("Financeiramente, " + s.getNome() + " se beneficiaria se a vítima desaparecesse.");
        }
        Collections.shuffle(pool);
        return pool.toArray(new String[0]);
    }

    // Pistas falsas DIFÍCEIS — quase indistinguíveis de pistas verdadeiras
    private static String[] buildPistasFalsasDificeis(List<Suspeito> inocentes, String localCrime) {
        List<String> pool = new ArrayList<>();
        for (Suspeito s : inocentes) {
            pool.add("Digital de " + s.getNome() + " encontrada no " + localCrime + ". Pode ter sido plantada.");
            pool.add("Câmera registra " + s.getNome() + " no corredor — o horário bate com o crime.");
            pool.add(s.getNome() + " não consegue explicar onde estava no horário do crime.");
            pool.add("Testemunha viu " + s.getNome() + " carregando um embrulho escuro. Objeto não encontrado.");
            pool.add(s.getNome() + " tinha acesso à chave do " + localCrime + " naquela noite.");
            pool.add("Ligação entre " + s.getNome() + " e a vítima foi detectada — conteúdo desconhecido.");
        }
        Collections.shuffle(pool);
        return pool.toArray(new String[0]);
    }

    // Álibis por dificuldade
    private static String alibiInocenteFacil() {
        String[] opcoes = {
            "Estava no restaurante com outros três hóspedes durante todo o jantar. Confirmado.",
            "Em ligação de vídeo com a família das 21h às 23h. Verificável pelo histórico.",
            "Registrado(a) na recepção às 22h pedindo toalhas extras. Funcionário confirma.",
            "Assistia à TV no quarto. O sistema de streaming registra atividade no horário."
        };
        return opcoes[random.nextInt(opcoes.length)];
    }

    private static String alibiInocenteMedio() {
        String[] opcoes = {
            "Afirma estar no jardim, mas nenhuma câmera cobre aquela área.",
            "Diz que dormia. Ninguém pode confirmar, mas também não há evidência contrária.",
            "Estava na garagem, segundo seu próprio relato. Câmera estava com defeito naquela noite.",
            "Afirma ter saído para caminhar. Voltou sem ser visto(a) por ninguém."
        };
        return opcoes[random.nextInt(opcoes.length)];
    }

    private static String alibiInocenteDificil() {
        String[] opcoes = {
            "Não soube precisar onde estava. Diz que 'estava por aí' sem mais detalhes.",
            "Muda a versão quando pressionado(a). Primeiro disse estar no quarto, depois no bar.",
            "Álibi depende de outra pessoa — que também é suspeita no caso.",
            "Afirma ter visto algo suspeito, mas a descrição contradiz a linha do tempo.",
            "Diz que estava dormindo, mas a câmera do corredor não registra entrada no quarto."
        };
        return opcoes[random.nextInt(opcoes.length)];
    }

    public static Caso gerar(String dificuldade) {

        // 1. Sorteia 5 suspeitos do pool
        List<String[]> pool = new ArrayList<>(Arrays.asList(PERSONAGENS));
        Collections.shuffle(pool);
        List<Suspeito> suspeitos = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            suspeitos.add(new Suspeito(pool.get(i)[0], pool.get(i)[1]));
        }

        // 2. Escolhe culpado e motivação
        Suspeito culpado = suspeitos.get(random.nextInt(suspeitos.size()));
        culpado.setCulpado(true);
        String motivacao = MOTIVACOES[random.nextInt(MOTIVACOES.length)];
        culpado.setMotivacao(motivacao);

        // 3. Linha do tempo conforme dificuldade
        String[] tempoPool = switch (dificuldade) {
            case "medio"   -> TEMPO_MEDIO;
            case "dificil" -> TEMPO_DIFICIL;
            default        -> TEMPO_FACIL;
        };
        String linhaDoTempo = tempoPool[random.nextInt(tempoPool.length)];

        // 4. Configuração de pistas por dificuldade (tabela do documento)
        int qtdVerdadeiras, qtdFalsas, qtdIndiretas;
        switch (dificuldade) {
            case "medio"   -> { qtdVerdadeiras = 4; qtdFalsas = 4; qtdIndiretas = 2; }
            case "dificil" -> { qtdVerdadeiras = 3; qtdFalsas = 6; qtdIndiretas = 1; }
            default        -> { qtdVerdadeiras = 5; qtdFalsas = 2; qtdIndiretas = 2; }
        }

        String localCrime = LOCAIS[random.nextInt(LOCAIS.length)];
        List<Pista> pistas = new ArrayList<>();

        // 5. Pistas verdadeiras — sorteadas do pool grande
        String[] poolVerdadeiras = buildPistasVerdadeiras(culpado, localCrime, motivacao);
        List<String> listaV = new ArrayList<>(Arrays.asList(poolVerdadeiras));
        Collections.shuffle(listaV);
        for (int i = 0; i < qtdVerdadeiras; i++) {
            pistas.add(new Pista(listaV.get(i), Pista.Tipo.VERDADEIRA, localCrime));
        }

        // 6. Pistas falsas — qualidade varia por dificuldade
        List<Suspeito> inocentes = new ArrayList<>(suspeitos);
        inocentes.remove(culpado);

        String[] poolFalsas = switch (dificuldade) {
            case "medio"   -> buildPistasFalsasMedias(inocentes, LOCAIS);
            case "dificil" -> buildPistasFalsasDificeis(inocentes, localCrime);
            default        -> buildPistasFalsasFaceis(inocentes, LOCAIS);
        };

        for (int i = 0; i < qtdFalsas && i < poolFalsas.length; i++) {
            String local = LOCAIS[random.nextInt(LOCAIS.length)];
            pistas.add(new Pista(poolFalsas[i], Pista.Tipo.FALSA, local));
        }

        // 7. Pistas indiretas — eliminam inocentes
        Collections.shuffle(inocentes);
        for (int i = 0; i < qtdIndiretas && i < inocentes.size(); i++) {
            Suspeito confirmado = inocentes.get(i);
            String[] textos = {
                "Câmera de segurança confirma que " + confirmado.getNome() + " estava no quarto durante o crime.",
                "Dois funcionários confirmam que " + confirmado.getNome() + " estava no restaurante no horário.",
                "Gravação de voz prova que " + confirmado.getNome() + " estava em ligação durante o crime."
            };
            String texto = textos[random.nextInt(textos.length)];
            pistas.add(new Pista(texto, Pista.Tipo.INDIRETA, "Recepção"));
        }

        // 8. Álibis — conforme dificuldade
        culpado.setAlibi("Afirma que estava dormindo no quarto, mas ninguém confirma sua presença.");
        for (Suspeito s : inocentes) {
            s.setAlibi(switch (dificuldade) {
                case "medio"   -> alibiInocenteMedio();
                case "dificil" -> alibiInocenteDificil();
                default        -> alibiInocenteFacil();
            });
        }

        Collections.shuffle(pistas);
        return new Caso(suspeitos, pistas, culpado, motivacao, linhaDoTempo, dificuldade);
    }
}
