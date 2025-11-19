package com.grupo5.domain.resultados;

import java.io.Serializable;
import java.util.List;

/**
 * @author pauloandre7
 * 
 * Classe modelo para armazenar o resultado das simulacoes e, com isso, permitir que o usuário consiga
 * realizar várias ao mesmo tempo e recuperar os detalhes de cada uma.
 */
public class Resultado implements Serializable{
    // Só pra respeitar a boa prática de guardar a versão para serialização
    private static final long serialVersionUID = 1L;

    // atributos de parametros da simulacao
    private double popTotal;
    private double taxaContagio;
    private double taxaRecuperacao;

    // atributos de resultados
    private List<Double> tempos;
    private List<Double> sucetiveisHistorico;
    private List<Double> infectadosHistorico;

    // construtor
    public Resultado(double popTotal, double taxaContagio, double taxaRecuperacao, List<Double> tempos, List<Double> sucetiveisHistorico, List<Double> infectadosHistorico){
        this.popTotal = popTotal;
        this.taxaContagio = taxaContagio;
        this.taxaRecuperacao = taxaRecuperacao;
        this.tempos = tempos;
        this.sucetiveisHistorico = sucetiveisHistorico;
        this.infectadosHistorico = infectadosHistorico;
    }

    //metodos
    public List<Double> getTempos(){
        return tempos;
    }

    public List<Double> getSucetiveisHistorico(){
        return sucetiveisHistorico;
    }

    public List<Double> getInfectadosHistorico(){
        return infectadosHistorico;
    }

    public double getPopTotal(){
        return popTotal;
    }

    public double getTaxaContagio(){
        return taxaContagio;
    }

    public double getTaxaRecuperacao(){
        return taxaRecuperacao;
    }

    public void setTempos(List<Double> tempos){
        this.tempos = tempos;
    }

    public void setSucetiveisHistorico(List<Double> sucetiveisHistorico){
        this.sucetiveisHistorico = sucetiveisHistorico;
    }

    public void setInfectadosHistorico(List<Double> infectadosHistorico){
        this.infectadosHistorico = infectadosHistorico;
    }

    public void setPopTotal(double popTotal){
        this.popTotal = popTotal;
    }

    public void setTaxaContagio(double taxaContagio){
        this.taxaContagio = taxaContagio;
    }

    public void setTaxaRecuperacao(double taxaRecuperacao){
        this.taxaRecuperacao = taxaRecuperacao;
    }
}
