/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.revenda.dao;

import com.revenda.model.Fabricante;
import com.revenda.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author antonio
 */
public class FabricanteDAO {
     // Inserir um fabricante no banco de dados
    public void salvar(Fabricante fabricante) throws SQLException {
        String sql = "INSERT INTO Fabricante (nome, pais, telefone) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, fabricante.getNome());
            stmt.setString(2, fabricante.getPais());
            stmt.setString(3, fabricante.getTelefone());
            stmt.executeUpdate();
        }
    }

    // Listar todos os fabricantes
    public List<Fabricante> listarTodos() throws SQLException {
        String sql = "SELECT * FROM Fabricante";
        List<Fabricante> fabricantes = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Fabricante fabricante = new Fabricante();
                fabricante.setId_fabricante(rs.getInt("id_fabricante"));
                fabricante.setNome(rs.getString("nome"));
                fabricante.setPais(rs.getString("pais"));
                fabricante.setTelefone(rs.getString("telefone"));
                fabricantes.add(fabricante);
            }
        }
        return fabricantes;
    }

    // Buscar um fabricante pelo ID
    public Fabricante buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Fabricante WHERE id_fabricante = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Fabricante fabricante = new Fabricante();
                    fabricante.setId_fabricante(rs.getInt("id_fabricante"));
                    fabricante.setNome(rs.getString("nome"));
                    fabricante.setPais(rs.getString("pais"));
                    fabricante.setTelefone(rs.getString("telefone"));
                    return fabricante;
                }
            }
        }
        return null; // Retorna null se não encontrar o fabricante
    }

    // Atualizar informações de um fabricante
    public void atualizar(Fabricante fabricante) throws SQLException {
        String sql = "UPDATE Fabricante SET nome = ?, pais = ?, telefone = ? WHERE id_fabricante = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, fabricante.getNome());
            stmt.setString(2, fabricante.getPais());
            stmt.setString(3, fabricante.getTelefone());
            stmt.setInt(4, fabricante.getId_fabricante());
            stmt.executeUpdate();
        }
    }

    // Excluir um fabricante pelo ID
    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM Fabricante WHERE id_fabricante = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
