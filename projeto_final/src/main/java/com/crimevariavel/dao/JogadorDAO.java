package com.crimevariavel.dao;


import com.crimevariavel.model.Jogador;
import com.crimevariavel.util.ConnectionFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class JogadorDAO {
    public void salvar(Jogador j) {
        String sql = "INSERT INTO jogadores (nome, moedas, pontos_upgrade, casos_resolvidos) VALUES (?,?,?,?)";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, j.getNome());
            ps.setInt(2, j.getMoedas());
            ps.setInt(3, j.getPontosUpgrade());
            ps.setInt(4, j.getCasosResolvidos());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) j.setId(rs.getInt(1));
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public Jogador buscarPorNome(String nome) {
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM jogadores WHERE nome=? LIMIT 1")) {
            ps.setString(1, nome);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public Jogador buscarPorId(int id) {
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM jogadores WHERE id=?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public void atualizar(Jogador j) {
        String sql = "UPDATE jogadores SET moedas=?, pontos_upgrade=?, casos_resolvidos=? WHERE id=?";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, j.getMoedas()); ps.setInt(2, j.getPontosUpgrade());
            ps.setInt(3, j.getCasosResolvidos()); ps.setInt(4, j.getId());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public List<Jogador> listarRanking() {
        List<Jogador> lista = new ArrayList<>();
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM jogadores ORDER BY casos_resolvidos DESC, moedas DESC");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    private Jogador mapear(ResultSet rs) throws SQLException {
        return new Jogador(rs.getInt("id"), rs.getString("nome"),
                rs.getInt("moedas"), rs.getInt("pontos_upgrade"), rs.getInt("casos_resolvidos"));
    }
}
