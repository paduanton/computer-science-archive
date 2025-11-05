/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.revenda.dao;

import com.revenda.model.Vendedor;
import com.revenda.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author antonio
 */
public class VendedorDAO {
    // Inserir um vendedor no banco de dados
    public void salvar(Vendedor vendedor) throws SQLException {
        String sql = "INSERT INTO Vendedor (nome, cpf, telefone, email, comissao) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, vendedor.getNome());
            stmt.setString(2, vendedor.getCpf());
            stmt.setString(3, vendedor.getTelefone());
            stmt.setString(4, vendedor.getEmail());
            stmt.setBigDecimal(5, vendedor.getComissao());
            stmt.executeUpdate();
        }
    }

    // Listar todos os vendedores
    public List<Vendedor> listarTodos() throws SQLException {
        String sql = "SELECT * FROM Vendedor";
        List<Vendedor> vendedores = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Vendedor vendedor = new Vendedor();
                vendedor.setId_vendedor(rs.getInt("id_vendedor"));
                vendedor.setNome(rs.getString("nome"));
                vendedor.setCpf(rs.getString("cpf"));
                vendedor.setTelefone(rs.getString("telefone"));
                vendedor.setEmail(rs.getString("email"));
                vendedor.setComissao(rs.getBigDecimal("comissao"));
                vendedores.add(vendedor);
            }
        }
        return vendedores;
    }

    // Buscar um vendedor pelo ID
    public Vendedor buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Vendedor WHERE id_vendedor = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Vendedor vendedor = new Vendedor();
                    vendedor.setId_vendedor(rs.getInt("id_vendedor"));
                    vendedor.setNome(rs.getString("nome"));
                    vendedor.setCpf(rs.getString("cpf"));
                    vendedor.setTelefone(rs.getString("telefone"));
                    vendedor.setEmail(rs.getString("email"));
                    vendedor.setComissao(rs.getBigDecimal("comissao"));
                    return vendedor;
                }
            }
        }
        return null; // Retorna null se não encontrar o vendedor
    }

    // Atualizar informações de um vendedor
    public void atualizar(Vendedor vendedor) throws SQLException {
        String sql = "UPDATE Vendedor SET nome = ?, cpf = ?, telefone = ?, email = ?, comissao = ? WHERE id_vendedor = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, vendedor.getNome());
            stmt.setString(2, vendedor.getCpf());
            stmt.setString(3, vendedor.getTelefone());
            stmt.setString(4, vendedor.getEmail());
            stmt.setBigDecimal(5, vendedor.getComissao());
            stmt.setInt(6, vendedor.getId_vendedor());
            stmt.executeUpdate();
        }
    }

    // Excluir um vendedor pelo ID
    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM Vendedor WHERE id_vendedor = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
