package com.grupo5.domain.resultados;

import java.util.List;

/**
 * @author pauloandre7
 * 
 * Classe modelo para armazenar o resultado das simulacoes e, com isso, permitir que o usuário consiga
 * realizar várias ao mesmo tempo e recuperar os detalhes de cada uma.
 */
public class Resultado {
    // atributos
    private List<Double> tempos;
    private List<Double> sucetiveisHistorico;
    private List<Double> infectadosHistorico;

    // construtor
    public Resultado(List<Double> tempos, List<Double> sucetiveisHistorico, List<Double> infectadosHistorico){
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

    public void setTempos(List<Double> tempos){
        this.tempos = tempos;
    }

    public void setSucetiveisHistorico(List<Double> sucetiveisHistorico){
        this.sucetiveisHistorico = sucetiveisHistorico;
    }

    public void setInfectadosHistorico(List<Double> infectadosHistorico){
        this.infectadosHistorico = infectadosHistorico;
    }
}
