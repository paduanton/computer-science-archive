/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.revenda.model;

import java.math.BigDecimal;

/**
 *
 * @author antonio
 */
public class Manutencao {
    private int id_manutencao;
    private int id_carro;
    private String data_manutencao;
    private String descricao;
    private BigDecimal custo;

    /**
     * @return the id_manutencao
     */
    public int getId_manutencao() {
        return id_manutencao;
    }

    /**
     * @param id_manutencao the id_manutencao to set
     */
    public void setId_manutencao(int id_manutencao) {
        this.id_manutencao = id_manutencao;
    }

    /**
     * @return the id_carro
     */
    public int getId_carro() {
        return id_carro;
    }

    /**
     * @param id_carro the id_carro to set
     */
    public void setId_carro(int id_carro) {
        this.id_carro = id_carro;
    }

    /**
     * @return the data_manutencao
     */
    public String getData_manutencao() {
        return data_manutencao;
    }

    /**
     * @param data_manutencao the data_manutencao to set
     */
    public void setData_manutencao(String data_manutencao) {
        this.data_manutencao = data_manutencao;
    }

    /**
     * @return the descricao
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * @param descricao the descricao to set
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * @return the custo
     */
    public BigDecimal getCusto() {
        return custo;
    }

    /**
     * @param custo the custo to set
     */
    public void setCusto(BigDecimal custo) {
        this.custo = custo;
    }
}
