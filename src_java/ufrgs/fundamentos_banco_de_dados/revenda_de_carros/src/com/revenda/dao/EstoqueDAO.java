/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.revenda.dao;

import com.revenda.model.Estoque;
import com.revenda.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author antonio
 */
public class EstoqueDAO {
     // Inserir um registro no estoque
    public void salvar(Estoque estoque) throws SQLException {
        String sql = "INSERT INTO Estoque (id_carro, quantidade, localizacao) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, estoque.getId_carro());
            stmt.setInt(2, estoque.getQuantidade());
            stmt.setString(3, estoque.getLocalizacao());
            stmt.executeUpdate();
        }
    }

    // Listar todos os registros do estoque
    public List<Estoque> listarTodos() throws SQLException {
        String sql = "SELECT * FROM Estoque";
        List<Estoque> estoques = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Estoque estoque = new Estoque();
                estoque.setId_estoque(rs.getInt("id_estoque"));
                estoque.setId_carro(rs.getInt("id_carro"));
                estoque.setQuantidade(rs.getInt("quantidade"));
                estoque.setLocalizacao(rs.getString("localizacao"));
                estoques.add(estoque);
            }
        }
        return estoques;
    }

    // Buscar um registro do estoque pelo ID
    public Estoque buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Estoque WHERE id_estoque = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Estoque estoque = new Estoque();
                    estoque.setId_estoque(rs.getInt("id_estoque"));
                    estoque.setId_carro(rs.getInt("id_carro"));
                    estoque.setQuantidade(rs.getInt("quantidade"));
                    estoque.setLocalizacao(rs.getString("localizacao"));
                    return estoque;
                }
            }
        }
        return null; // Retorna null se não encontrar o registro
    }

    // Atualizar um registro do estoque
    public void atualizar(Estoque estoque) throws SQLException {
        String sql = "UPDATE Estoque SET id_carro = ?, quantidade = ?, localizacao = ? WHERE id_estoque = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, estoque.getId_carro());
            stmt.setInt(2, estoque.getQuantidade());
            stmt.setString(3, estoque.getLocalizacao());
            stmt.setInt(4, estoque.getId_estoque());
            stmt.executeUpdate();
        }
    }

    // Excluir um registro do estoque pelo ID
    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM Estoque WHERE id_estoque = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
