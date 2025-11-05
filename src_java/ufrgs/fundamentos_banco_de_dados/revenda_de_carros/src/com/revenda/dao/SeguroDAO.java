/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.revenda.dao;

import com.revenda.util.DatabaseConnection;
import com.revenda.model.Seguro;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author antonio
 */
public class SeguroDAO {
    public void salvar(Seguro seguro) throws SQLException {
        String sql = """
                INSERT INTO Seguros (id_cliente, id_carro, valor_pago, valor_seguro, tipo_seguro, data_inicio, data_fim)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection conn = DatabaseConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, seguro.getIdCliente());
            stmt.setInt(2, seguro.getIdCarro());
            stmt.setDouble(3, seguro.getValorPago());
            stmt.setDouble(4, seguro.getValorSeguro());
            stmt.setString(5, seguro.getTipoSeguro());
            stmt.setDate(6, new java.sql.Date(seguro.getDataInicio().getTime()));
            stmt.setDate(7, new java.sql.Date(seguro.getDataFim().getTime()));
            stmt.executeUpdate();
        }
    }

    public List<Seguro> listarTodos() throws SQLException {
        String sql = "SELECT * FROM Seguros";
        List<Seguro> lista = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Seguro seguro = new Seguro();
                seguro.setIdSeguro(rs.getInt("id_seguro"));
                seguro.setIdCliente(rs.getInt("id_cliente"));
                seguro.setIdCarro(rs.getInt("id_carro"));
                seguro.setValorPago(rs.getDouble("valor_pago"));
                seguro.setValorSeguro(rs.getDouble("valor_seguro"));
                seguro.setTipoSeguro(rs.getString("tipo_seguro"));
                seguro.setDataInicio(rs.getDate("data_inicio"));
                seguro.setDataFim(rs.getDate("data_fim"));
                lista.add(seguro);
            }
        }
        return lista;
    }
}
