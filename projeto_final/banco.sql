-- ==============================================
-- SCRIPT DE CRIAÇÃO DO BANCO — Crime Variável
-- ==============================================
-- 1. Crie o banco antes de rodar:
--    CREATE DATABASE crimevariavel;
-- 2. Conecte ao banco e rode este script.
-- ==============================================


-- -----------------------------------------------
-- TABELA: jogadores
-- Armazena os dados de cada jogador cadastrado.
-- -----------------------------------------------
CREATE TABLE IF NOT EXISTS jogadores (
    id                SERIAL PRIMARY KEY,
    nome              VARCHAR(100) NOT NULL,
    moedas            INTEGER DEFAULT 0,
    pontos_upgrade    INTEGER DEFAULT 0,
    casos_resolvidos  INTEGER DEFAULT 0,
    criado_em         TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- -----------------------------------------------
-- TABELA: partidas
-- Registra o histórico de cada run jogada.
-- -----------------------------------------------
CREATE TABLE IF NOT EXISTS partidas (
    id           SERIAL PRIMARY KEY,
    id_jogador   INTEGER NOT NULL REFERENCES jogadores(id),
    resultado    VARCHAR(10) CHECK (resultado IN ('vitoria', 'derrota')),
    pontuacao    INTEGER DEFAULT 0,
    dificuldade  VARCHAR(10) DEFAULT 'facil' CHECK (dificuldade IN ('facil', 'medio', 'dificil')),
    jogada_em    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- -----------------------------------------------
-- TABELA: upgrades
-- Guarda quais upgrades permanentes cada jogador comprou.
-- UNIQUE garante que o mesmo upgrade não seja comprado duas vezes
-- pelo mesmo jogador — necessário para o ON CONFLICT DO NOTHING
-- funcionar corretamente no UpgradeDAO.
-- -----------------------------------------------
CREATE TABLE IF NOT EXISTS upgrades (
    id           SERIAL PRIMARY KEY,
    id_jogador   INTEGER NOT NULL REFERENCES jogadores(id),
    tipo         VARCHAR(50) NOT NULL,
    nivel        INTEGER DEFAULT 1,
    adquirido_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT unique_upgrade UNIQUE (id_jogador, tipo)
);


-- -----------------------------------------------
-- TABELA: itens_jogador
-- Registra os itens comprados na loja por cada jogador.
-- Permite acumular quantidade do mesmo item.
-- -----------------------------------------------
CREATE TABLE IF NOT EXISTS itens_jogador (
    id           SERIAL PRIMARY KEY,
    id_jogador   INTEGER NOT NULL REFERENCES jogadores(id),
    nome_item    VARCHAR(100) NOT NULL,
    quantidade   INTEGER DEFAULT 1,
    adquirido_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- -----------------------------------------------
-- Verificação: lista as tabelas criadas
-- -----------------------------------------------
SELECT table_name 
FROM information_schema.tables 
WHERE table_schema = 'public'
ORDER BY table_name;
