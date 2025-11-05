/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.revenda.dao;

import com.revenda.model.Carro;
import com.revenda.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author antonio
 */
public class CarroDAO {

    public void salvar(Carro carro) throws SQLException {
        String sql = "INSERT INTO Carro (modelo, marca, ano, valor, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, carro.getModelo());
            stmt.setString(2, carro.getMarca());
            stmt.setInt(3, carro.getAno());
            stmt.setBigDecimal(4, carro.getValor());
            stmt.setString(5, carro.getStatus());
            stmt.executeUpdate();
        }
    }

    public List<Carro> listarTodos() throws SQLException {
        String sql = "SELECT * FROM Carro";
        List<Carro> carros = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Carro carro = new Carro();
                carro.setId_carro(rs.getInt("id_carro"));
                carro.setModelo(rs.getString("modelo"));
                carro.setMarca(rs.getString("marca"));
                carro.setAno(rs.getInt("ano"));
                carro.setValor(rs.getBigDecimal("valor"));
                carro.setStatus(rs.getString("status"));
                carros.add(carro);
            }
        }
        return carros;
    }

    public Carro buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Carro WHERE id_carro = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Carro carro = new Carro();
                    carro.setId_carro(rs.getInt("id_carro"));
                    carro.setModelo(rs.getString("modelo"));
                    carro.setMarca(rs.getString("marca"));
                    carro.setAno(rs.getInt("ano"));
                    carro.setValor(rs.getBigDecimal("valor"));
                    carro.setStatus(rs.getString("status"));
                    return carro;
                }
            }
        }
        return null;
    }

    public void atualizar(Carro carro) throws SQLException {
        String sql = "UPDATE Carro SET modelo = ?, marca = ?, ano = ?, valor = ?, status = ? WHERE id_carro = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, carro.getModelo());
            stmt.setString(2, carro.getMarca());
            stmt.setInt(3, carro.getAno());
            stmt.setBigDecimal(4, carro.getValor());
            stmt.setString(5, carro.getStatus());
            stmt.setInt(6, carro.getId_carro());
            stmt.executeUpdate();
        }
    }

    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM Carro WHERE id_carro = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
