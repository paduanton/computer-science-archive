/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.revenda.dao;

import com.revenda.model.Pagamento;
import com.revenda.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author antonio
 */
public class PagamentoDAO {
     // Inserir um pagamento no banco de dados
    public void salvar(Pagamento pagamento) throws SQLException {
        String sql = "INSERT INTO Pagamento (id_venda, valor_pago, data_pagamento, forma_pagamento) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pagamento.getId_venda());
            stmt.setBigDecimal(2, pagamento.getValor_pago());
            stmt.setDate(3, Date.valueOf(pagamento.getData_pagamento()));
            stmt.setString(4, pagamento.getForma_pagamento());
            stmt.executeUpdate();
        }
    }

    // Listar todos os pagamentos
    public List<Pagamento> listarTodos() throws SQLException {
        String sql = "SELECT * FROM Pagamento";
        List<Pagamento> pagamentos = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Pagamento pagamento = new Pagamento();
                pagamento.setId_pagamento(rs.getInt("id_pagamento"));
                pagamento.setId_venda(rs.getInt("id_venda"));
                pagamento.setValor_pago(rs.getBigDecimal("valor_pago"));
                pagamento.setData_pagamento(rs.getDate("data_pagamento").toLocalDate()+"");
                pagamento.setForma_pagamento(rs.getString("forma_pagamento"));
                pagamentos.add(pagamento);
            }
        }
        return pagamentos;
    }

    // Buscar um pagamento pelo ID
    public Pagamento buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Pagamento WHERE id_pagamento = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Pagamento pagamento = new Pagamento();
                    pagamento.setId_pagamento(rs.getInt("id_pagamento"));
                    pagamento.setId_venda(rs.getInt("id_venda"));
                    pagamento.setValor_pago(rs.getBigDecimal("valor_pago"));
                    pagamento.setData_pagamento(rs.getDate("data_pagamento").toLocalDate()+"");
                    pagamento.setForma_pagamento(rs.getString("forma_pagamento"));
                    return pagamento;
                }
            }
        }
        return null; // Retorna null se não encontrar
    }

    // Atualizar um pagamento existente
    public void atualizar(Pagamento pagamento) throws SQLException {
        String sql = "UPDATE Pagamento SET id_venda = ?, valor_pago = ?, data_pagamento = ?, forma_pagamento = ? WHERE id_pagamento = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pagamento.getId_venda());
            stmt.setBigDecimal(2, pagamento.getValor_pago());
            stmt.setDate(3, Date.valueOf(pagamento.getData_pagamento()));
            stmt.setString(4, pagamento.getForma_pagamento());
            stmt.setInt(5, pagamento.getId_pagamento());
            stmt.executeUpdate();
        }
    }

    // Excluir um pagamento pelo ID
    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM Pagamento WHERE id_pagamento = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
