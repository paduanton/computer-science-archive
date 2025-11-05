/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.revenda.dao;

import com.revenda.model.Manutencao;
import com.revenda.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author antonio
 */
public class ManutencaoDAO {
    // Inserir uma manutenção no banco de dados

    public void salvar(Manutencao manutencao) throws SQLException {
        String sql = "INSERT INTO Manutencao (id_carro, data_manutencao, descricao, custo) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, manutencao.getId_carro());
            stmt.setDate(2, Date.valueOf(manutencao.getData_manutencao()));
            stmt.setString(3, manutencao.getDescricao());
            stmt.setBigDecimal(4, manutencao.getCusto());
            stmt.executeUpdate();
        }
    }

    // Listar todas as manutenções
    public List<Manutencao> listarTodos() throws SQLException {
        String sql = "SELECT * FROM Manutencao";
        List<Manutencao> manutencoes = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Manutencao manutencao = new Manutencao();
                manutencao.setId_manutencao(rs.getInt("id_manutencao"));
                manutencao.setId_carro(rs.getInt("id_carro"));
                manutencao.setData_manutencao(rs.getDate("data_manutencao").toLocalDate()+"");
                manutencao.setDescricao(rs.getString("descricao"));
                manutencao.setCusto(rs.getBigDecimal("custo"));
                manutencoes.add(manutencao);
            }
        }
        return manutencoes;
    }

    // Buscar uma manutenção pelo ID
    public Manutencao buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Manutencao WHERE id_manutencao = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Manutencao manutencao = new Manutencao();
                    manutencao.setId_manutencao(rs.getInt("id_manutencao"));
                    manutencao.setId_carro(rs.getInt("id_carro"));
                    manutencao.setData_manutencao(rs.getDate("data_manutencao").toLocalDate()+"");
                    manutencao.setDescricao(rs.getString("descricao"));
                    manutencao.setCusto(rs.getBigDecimal("custo"));
                    return manutencao;
                }
            }
        }
        return null; // Retorna null se não encontrar
    }

    // Atualizar uma manutenção existente
    public void atualizar(Manutencao manutencao) throws SQLException {
        String sql = "UPDATE Manutencao SET id_carro = ?, data_manutencao = ?, descricao = ?, custo = ? WHERE id_manutencao = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, manutencao.getId_carro());
            stmt.setDate(2, Date.valueOf(manutencao.getData_manutencao()));
            stmt.setString(3, manutencao.getDescricao());
            stmt.setBigDecimal(4, manutencao.getCusto());
            stmt.setInt(5, manutencao.getId_manutencao());
            stmt.executeUpdate();
        }
    }

    // Excluir uma manutenção pelo ID
    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM Manutencao WHERE id_manutencao = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
