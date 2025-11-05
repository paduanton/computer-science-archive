/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.revenda.model;

import java.util.Date;

/**
 *
 * @author antonio
 */
public class Financiamento {
     private int idFinanciamento;
    private int idCliente;
    private int idVenda;
    private double valorFinanciado;
    private double taxaJuros;
    private int parcelas;
    private Date dataInicio;

    /**
     * @return the idFinanciamento
     */
    public int getIdFinanciamento() {
        return idFinanciamento;
    }

    /**
     * @param idFinanciamento the idFinanciamento to set
     */
    public void setIdFinanciamento(int idFinanciamento) {
        this.idFinanciamento = idFinanciamento;
    }

    /**
     * @return the idCliente
     */
    public int getIdCliente() {
        return idCliente;
    }

    /**
     * @param idCliente the idCliente to set
     */
    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    /**
     * @return the idVenda
     */
    public int getIdVenda() {
        return idVenda;
    }

    /**
     * @param idVenda the idVenda to set
     */
    public void setIdVenda(int idVenda) {
        this.idVenda = idVenda;
    }

    /**
     * @return the valorFinanciado
     */
    public double getValorFinanciado() {
        return valorFinanciado;
    }

    /**
     * @param valorFinanciado the valorFinanciado to set
     */
    public void setValorFinanciado(double valorFinanciado) {
        this.valorFinanciado = valorFinanciado;
    }

    /**
     * @return the taxaJuros
     */
    public double getTaxaJuros() {
        return taxaJuros;
    }

    /**
     * @param taxaJuros the taxaJuros to set
     */
    public void setTaxaJuros(double taxaJuros) {
        this.taxaJuros = taxaJuros;
    }

    /**
     * @return the parcelas
     */
    public int getParcelas() {
        return parcelas;
    }

    /**
     * @param parcelas the parcelas to set
     */
    public void setParcelas(int parcelas) {
        this.parcelas = parcelas;
    }

    /**
     * @return the dataInicio
     */
    public Date getDataInicio() {
        return dataInicio;
    }

    /**
     * @param dataInicio the dataInicio to set
     */
    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }
}
