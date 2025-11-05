/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.revenda.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author antonio
 */
public class DatabaseInitializer {

    public static void criarTabelas() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Verificar e criar tabelas
            if (!tabelaExiste(conn, "Carro")) {
                criarTabelaCarro(conn);
            }
            if (!tabelaExiste(conn, "Cliente")) {
                criarTabelaCliente(conn);
            }
            if (!tabelaExiste(conn, "Vendedor")) {
                criarTabelaVendedor(conn);
            }
            if (!tabelaExiste(conn, "Venda")) {
                criarTabelaVenda(conn);
            }
            if (!tabelaExiste(conn, "Estoque")) {
                criarTabelaEstoque(conn);
            }
            if (!tabelaExiste(conn, "Fabricante")) {
                criarTabelaFabricante(conn);
            }
            if (!tabelaExiste(conn, "Manutencao")) {
                criarTabelaManutencao(conn);
            }
            if (!tabelaExiste(conn, "Pagamento")) {
                criarTabelaPagamento(conn);
            }
            if (!tabelaExiste(conn, "Seguros")) {
                criarTabelaSeguros(conn);
            }
            if (!tabelaExiste(conn, "Financiamentos")) {
                criarTabelaFinanciamentos(conn);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean tabelaExiste(Connection conn, String nomeTabela) throws SQLException {
        DatabaseMetaData metadata = conn.getMetaData();
        try (ResultSet rs = metadata.getTables(null, null, nomeTabela, null)) {
            return rs.next();
        }
    }

    private static void criarTabelaCarro(Connection conn) throws SQLException {
        String sql = """
                CREATE TABLE Carro (
                    id_carro INT AUTO_INCREMENT PRIMARY KEY,
                    modelo VARCHAR(255),
                    marca VARCHAR(255),
                    ano INT,
                    valor DECIMAL(10, 2),
                    status ENUM('disponivel', 'vendido')
                )
                """;
        executarSQL(conn, sql);
    }

    private static void criarTabelaCliente(Connection conn) throws SQLException {
        String sql = """
                CREATE TABLE Cliente (
                    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
                    nome VARCHAR(255),
                    cpf VARCHAR(11),
                    telefone VARCHAR(15),
                    email VARCHAR(255),
                    endereco TEXT
                )
                """;
        executarSQL(conn, sql);
    }

    private static void criarTabelaVendedor(Connection conn) throws SQLException {
        String sql = """
                CREATE TABLE Vendedor (
                    id_vendedor INT AUTO_INCREMENT PRIMARY KEY,
                    nome VARCHAR(255),
                    cpf VARCHAR(11),
                    telefone VARCHAR(15),
                    email VARCHAR(255),
                    comissao DECIMAL(5, 2)
                )
                """;
        executarSQL(conn, sql);
    }

    private static void criarTabelaVenda(Connection conn) throws SQLException {
        String sql = """
                CREATE TABLE Venda (
                    id_venda INT AUTO_INCREMENT PRIMARY KEY,
                    id_cliente INT,
                    id_vendedor INT,
                    id_carro INT,
                    data_venda DATE,
                    valor_total DECIMAL(10, 2),
                    FOREIGN KEY (id_cliente) REFERENCES Cliente(id_cliente),
                    FOREIGN KEY (id_vendedor) REFERENCES Vendedor(id_vendedor),
                    FOREIGN KEY (id_carro) REFERENCES Carro(id_carro)
                )
                """;
        executarSQL(conn, sql);
    }

    private static void criarTabelaEstoque(Connection conn) throws SQLException {
        String sql = """
                CREATE TABLE Estoque (
                    id_estoque INT AUTO_INCREMENT PRIMARY KEY,
                    id_carro INT,
                    quantidade INT,
                    localizacao VARCHAR(255),
                    FOREIGN KEY (id_carro) REFERENCES Carro(id_carro)
                )
                """;
        executarSQL(conn, sql);
    }

    private static void criarTabelaFabricante(Connection conn) throws SQLException {
        String sql = """
                CREATE TABLE Fabricante (
                    id_fabricante INT AUTO_INCREMENT PRIMARY KEY,
                    nome VARCHAR(255),
                    pais VARCHAR(255),
                    telefone VARCHAR(15)
                )
                """;
        executarSQL(conn, sql);
    }

    private static void criarTabelaManutencao(Connection conn) throws SQLException {
        String sql = """
                CREATE TABLE Manutencao (
                    id_manutencao INT AUTO_INCREMENT PRIMARY KEY,
                    id_carro INT,
                    data_manutencao DATE,
                    descricao TEXT,
                    custo DECIMAL(10, 2),
                    FOREIGN KEY (id_carro) REFERENCES Carro(id_carro)
                )
                """;
        executarSQL(conn, sql);
    }

    private static void criarTabelaPagamento(Connection conn) throws SQLException {
        String sql = """
                CREATE TABLE Pagamento (
                    id_pagamento INT AUTO_INCREMENT PRIMARY KEY,
                    id_venda INT,
                    valor_pago DECIMAL(10, 2),
                    data_pagamento DATE,
                    forma_pagamento ENUM('cartao', 'dinheiro', 'financiamento'),
                    FOREIGN KEY (id_venda) REFERENCES Venda(id_venda)
                )
                """;
        executarSQL(conn, sql);
    }
    private static void criarTabelaSeguros(Connection conn) throws SQLException {
        String sql = """
                CREATE TABLE Seguros (
                    id_seguro INT AUTO_INCREMENT PRIMARY KEY,
                    id_cliente INT,
                    id_carro INT,
                    valor_pago DECIMAL(10, 2),
                    valor_seguro DECIMAL(10, 2),
                    tipo_seguro VARCHAR(255),
                    data_inicio DATE,
                    data_fim DATE,
                    FOREIGN KEY (id_cliente) REFERENCES Cliente(id_cliente),
                    FOREIGN KEY (id_carro) REFERENCES Carro(id_carro)
                )
                """;
        executarSQL(conn, sql);
    }

    private static void criarTabelaFinanciamentos(Connection conn) throws SQLException {
        String sql = """
                CREATE TABLE Financiamentos (
                    id_financiamento INT AUTO_INCREMENT PRIMARY KEY,
                    id_cliente INT,
                    id_venda INT,
                    valor_financiado DECIMAL(10, 2),
                    taxa_juros DECIMAL(5, 2),
                    parcelas INT,
                    data_inicio DATE,
                    FOREIGN KEY (id_cliente) REFERENCES Cliente(id_cliente),
                    FOREIGN KEY (id_venda) REFERENCES Venda(id_venda)
                )
                """;
        executarSQL(conn, sql);
    }
    private static void executarSQL(Connection conn, String sql) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
            System.out.println("Tabela criada com sucesso: " + sql.substring(13, sql.indexOf("(")).trim());
        }
    }
}
