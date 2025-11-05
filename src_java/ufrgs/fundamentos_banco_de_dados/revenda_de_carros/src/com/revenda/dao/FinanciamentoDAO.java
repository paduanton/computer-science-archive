/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.revenda.dao;

import com.revenda.model.Financiamento;
import com.revenda.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author antonio
 */
public class FinanciamentoDAO {
    public void salvar(Financiamento financiamento) throws SQLException {
        String sql = """
                INSERT INTO Financiamentos (id_cliente, id_venda, valor_financiado, taxa_juros, parcelas, data_inicio)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, financiamento.getIdCliente());
            stmt.setInt(2, financiamento.getIdVenda());
            stmt.setDouble(3, financiamento.getValorFinanciado());
            stmt.setDouble(4, financiamento.getTaxaJuros());
            stmt.setInt(5, financiamento.getParcelas());
            stmt.setDate(6, new java.sql.Date(financiamento.getDataInicio().getTime()));
            stmt.executeUpdate();
        }
    }

    public List<Financiamento> listarTodos() throws SQLException {
        String sql = "SELECT * FROM Financiamentos";
        List<Financiamento> lista = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Financiamento financiamento = new Financiamento();
                financiamento.setIdFinanciamento(rs.getInt("id_financiamento"));
                financiamento.setIdCliente(rs.getInt("id_cliente"));
                financiamento.setIdVenda(rs.getInt("id_venda"));
                financiamento.setValorFinanciado(rs.getDouble("valor_financiado"));
                financiamento.setTaxaJuros(rs.getDouble("taxa_juros"));
                financiamento.setParcelas(rs.getInt("parcelas"));
                financiamento.setDataInicio(rs.getDate("data_inicio"));
                lista.add(financiamento);
            }
        }
        return lista;
    }
}
