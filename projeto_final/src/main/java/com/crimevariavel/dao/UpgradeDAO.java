package com.crimevariavel.dao;

import com.crimevariavel.util.ConnectionFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class UpgradeDAO {
    public void salvar(int idJogador, String tipo) {
        String sql = "INSERT INTO upgrades (id_jogador, tipo) VALUES (?,?) " +
                     "ON CONFLICT ON CONSTRAINT unique_upgrade DO NOTHING";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idJogador);
            ps.setString(2, tipo);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public List<String> listarPorJogador(int idJogador) {
        List<String> lista = new ArrayList<>();
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT tipo FROM upgrades WHERE id_jogador=?")) {
            ps.setInt(1, idJogador);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(rs.getString("tipo"));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }
}
