/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.revenda.dao;

import com.revenda.model.Venda;
import com.revenda.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author antonio
 */
public class VendaDAO {
    // Inserir uma venda no banco de dados

    public void salvar(Venda venda) throws SQLException {
        String sql = "INSERT INTO Venda (id_cliente, id_vendedor, id_carro, data_venda, valor_total) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, venda.getId_cliente());
            stmt.setInt(2, venda.getId_vendedor());
            stmt.setInt(3, venda.getId_carro());
            stmt.setDate(4, Date.valueOf(venda.getData_venda()));
            stmt.setBigDecimal(5, venda.getValor_total());
            stmt.executeUpdate();

            // Obter o ID gerado
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    venda.setId_venda(rs.getInt(1));
                }
            }
        }
    }

    // Listar todas as vendas
    public List<Venda> listarTodas() throws SQLException {
        String sql = "SELECT * FROM Venda";
        List<Venda> vendas = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Venda venda = new Venda();
                venda.setId_venda(rs.getInt("id_venda"));
                venda.setId_cliente(rs.getInt("id_cliente"));
                venda.setId_vendedor(rs.getInt("id_vendedor"));
                venda.setId_carro(rs.getInt("id_carro"));
                venda.setData_venda(rs.getDate("data_venda").toLocalDate() + "");
                venda.setValor_total(rs.getBigDecimal("valor_total"));
                vendas.add(venda);
            }
        }
        return vendas;
    }

    // Buscar uma venda pelo ID
    public Venda buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Venda WHERE id_venda = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Venda venda = new Venda();
                    venda.setId_venda(rs.getInt("id_venda"));
                    venda.setId_cliente(rs.getInt("id_cliente"));
                    venda.setId_vendedor(rs.getInt("id_vendedor"));
                    venda.setId_carro(rs.getInt("id_carro"));
                    venda.setData_venda(rs.getDate("data_venda").toLocalDate() + "");
                    venda.setValor_total(rs.getBigDecimal("valor_total"));
                    return venda;
                }
            }
        }
        return null; // Retorna null se não encontrar
    }

    // Atualizar uma venda existente
    public void atualizar(Venda venda) throws SQLException {
        String sql = "UPDATE Venda SET id_cliente = ?, id_vendedor = ?, id_carro = ?, data_venda = ?, valor_total = ? WHERE id_venda = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, venda.getId_cliente());
            stmt.setInt(2, venda.getId_vendedor());
            stmt.setInt(3, venda.getId_carro());
            stmt.setDate(4, Date.valueOf(venda.getData_venda()));
            stmt.setBigDecimal(5, venda.getValor_total());
            stmt.setInt(6, venda.getId_venda());
            stmt.executeUpdate();
        }
    }

    // Deletar uma venda pelo ID
    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM Venda WHERE id_venda = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Consulta para rastrear as vendas realizadas por cada funcionário
    public List<String> rastrearVendasPorFuncionario() throws SQLException {
        String sql = """
                SELECT vendedor.nome, COUNT(venda.id_venda) AS total_vendas,
                       SUM(venda.valor_total) AS total_valor_vendas
                FROM venda
                JOIN vendedor ON venda.id_vendedor = vendedor.id_vendedor
                GROUP BY vendedor.nome
                ORDER BY total_vendas DESC;
                """;

        List<String> resultados = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection(); 
                PreparedStatement stmt = connection.prepareStatement(sql); 
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String nome = rs.getString("nome");
                int totalVendas = rs.getInt("total_vendas");
                double totalValorVendas = rs.getDouble("total_valor_vendas");
                resultados.add(String.format("Vendedor: %s, Total Vendas: %d, Valor Total: %.2f",
                        nome, totalVendas, totalValorVendas));
            }
        }
        return resultados;
    }

    // Consulta para identificar os carros mais vendidos
    public List<String> identificarCarrosMaisVendidos() throws SQLException {
        String sql = """
                SELECT carro.modelo, carro.marca, COUNT(venda.id_carro) AS total_vendas
                FROM venda
                JOIN carro ON venda.id_carro = carro.id_carro
                GROUP BY carro.modelo, carro.marca
                ORDER BY total_vendas DESC;
                """;

        List<String> resultados = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection(); 
                PreparedStatement stmt = connection.prepareStatement(sql); 
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String modelo = rs.getString("modelo");
                String marca = rs.getString("marca");
                int totalVendas = rs.getInt("total_vendas");
                resultados.add(String.format("Modelo: %s, Marca: %s, Total Vendas: %d",
                        modelo, marca, totalVendas));
            }
        }
        return resultados;
    }
}
