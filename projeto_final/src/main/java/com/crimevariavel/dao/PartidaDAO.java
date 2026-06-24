package com.crimevariavel.dao;
import com.crimevariavel.util.ConnectionFactory;
import java.sql.*;

public class PartidaDAO {
    public void salvar(int idJogador, String resultado, int pontuacao, String dificuldade) {
        String sql = "INSERT INTO partidas (id_jogador, resultado, pontuacao, dificuldade) VALUES (?,?,?,?)";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idJogador);
            ps.setString(2, resultado);
            ps.setInt(3, pontuacao);
            ps.setString(4, dificuldade);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
