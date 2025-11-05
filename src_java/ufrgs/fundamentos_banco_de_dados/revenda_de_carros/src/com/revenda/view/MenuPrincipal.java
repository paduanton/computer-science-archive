/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.revenda.view;

import com.revenda.controller.VendaController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Scanner;

/**
 *
 * @author antonio
 */

public class MenuPrincipal {

    public static void main(String[] args) {
        VendaController vendaController = new VendaController();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("===== Sistema de Revenda de Veículos =====");
            System.out.println("1. Adicionar Venda");
            System.out.println("2. Listar Vendas");
            System.out.println("3. Buscar Venda por ID");
            System.out.println("4. Atualizar Venda");
            System.out.println("5. Remover Venda");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine(); // Limpar o buffer

            try {
                switch (opcao) {
                    case 1:
                        System.out.print("ID do Cliente: ");
                        int idCliente = scanner.nextInt();

                        System.out.print("ID do Vendedor: ");
                        int idVendedor = scanner.nextInt();

                        System.out.print("ID do Carro: ");
                        int idCarro = scanner.nextInt();

                        System.out.print("Data da Venda (AAAA-MM-DD): ");
                        String dataStr = scanner.next();
                        LocalDate dataVenda = LocalDate.parse(dataStr);

                        System.out.print("Valor Total: ");
                        BigDecimal valorTotal = scanner.nextBigDecimal();

                        vendaController.adicionarVenda(idCliente, idVendedor, idCarro, dataVenda, valorTotal);
                        System.out.println("Venda adicionada com sucesso!");
                        break;

                    case 2:
                        vendaController.listarVendas().forEach(v -> {
                            System.out.println("ID Venda: " + v.getId_venda());
                            System.out.println("Cliente: " + v.getId_cliente());
                            System.out.println("Vendedor: " + v.getId_vendedor());
                            System.out.println("Carro: " + v.getId_carro());
                            System.out.println("Data: " + v.getData_venda());
                            System.out.println("Valor: " + v.getValor_total());
                            System.out.println("-----------------------");
                        });
                        break;

                    case 3:
                        System.out.print("ID da Venda: ");
                        int idVendaBusca = scanner.nextInt();
                        var vendaEncontrada = vendaController.buscarVendaPorId(idVendaBusca);
                        if (vendaEncontrada != null) {
                            System.out.println("ID Venda: " + vendaEncontrada.getId_venda());
                            System.out.println("Cliente: " + vendaEncontrada.getId_cliente());
                            System.out.println("Vendedor: " + vendaEncontrada.getId_vendedor());
                            System.out.println("Carro: " + vendaEncontrada.getId_carro());
                            System.out.println("Data: " + vendaEncontrada.getData_venda());
                            System.out.println("Valor: " + vendaEncontrada.getValor_total());
                        } else {
                            System.out.println("Venda não encontrada!");
                        }
                        break;

                    case 4:
                        System.out.print("ID da Venda a ser atualizada: ");
                        int idVendaAtualizar = scanner.nextInt();

                        System.out.print("Novo ID do Cliente: ");
                        int novoIdCliente = scanner.nextInt();

                        System.out.print("Novo ID do Vendedor: ");
                        int novoIdVendedor = scanner.nextInt();

                        System.out.print("Novo ID do Carro: ");
                        int novoIdCarro = scanner.nextInt();

                        System.out.print("Nova Data da Venda (AAAA-MM-DD): ");
                        String novaDataStr = scanner.next();
                        LocalDate novaDataVenda = LocalDate.parse(novaDataStr);

                        System.out.print("Novo Valor Total: ");
                        BigDecimal novoValorTotal = scanner.nextBigDecimal();

                        vendaController.atualizarVenda(idVendaAtualizar, novoIdCliente, novoIdVendedor, novoIdCarro, novaDataVenda, novoValorTotal);
                        System.out.println("Venda atualizada com sucesso!");
                        break;

                    case 5:
                        System.out.print("ID da Venda a ser removida: ");
                        int idVendaRemover = scanner.nextInt();
                        vendaController.removerVenda(idVendaRemover);
                        System.out.println("Venda removida com sucesso!");
                        break;

                    case 0:
                        System.out.println("Saindo do sistema...");
                        scanner.close();
                        return;

                    default:
                        System.out.println("Opção inválida!");
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }
}

