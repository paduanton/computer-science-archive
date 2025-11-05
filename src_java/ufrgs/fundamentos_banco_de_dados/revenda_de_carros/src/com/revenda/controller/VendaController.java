/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.revenda.controller;

import com.revenda.dao.VendaDAO;
import com.revenda.model.Venda;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author antonio
 */
public class VendaController {

    private VendaDAO vendaDAO;

    public VendaController() {
        this.vendaDAO = new VendaDAO();
    }

    // Adicionar uma nova venda
    public void adicionarVenda(int idCliente, int idVendedor, int idCarro, LocalDate dataVenda, BigDecimal valorTotal) throws SQLException {
        Venda venda = new Venda();
        venda.setId_cliente(idCliente);
        venda.setId_vendedor(idVendedor);
        venda.setId_carro(idCarro);
        venda.setData_venda(dataVenda+"");
        venda.setValor_total(valorTotal);
        vendaDAO.salvar(venda);
    }

    // Listar todas as vendas
    public List<Venda> listarVendas() throws SQLException {
        return vendaDAO.listarTodas();
    }

    // Buscar venda por ID
    public Venda buscarVendaPorId(int id) throws SQLException {
        return vendaDAO.buscarPorId(id);
    }

    // Atualizar uma venda existente
    public void atualizarVenda(int idVenda, int idCliente, int idVendedor, int idCarro, LocalDate dataVenda, BigDecimal valorTotal) throws SQLException {
        Venda venda = vendaDAO.buscarPorId(idVenda);
        if (venda != null) {
            venda.setId_cliente(idCliente);
            venda.setId_vendedor(idVendedor);
            venda.setId_carro(idCarro);
            venda.setData_venda(dataVenda+"");
            venda.setValor_total(valorTotal);
            vendaDAO.atualizar(venda);
        }
    }

    // Remover uma venda pelo ID
    public void removerVenda(int id) throws SQLException {
        vendaDAO.deletar(id);
    }
}

