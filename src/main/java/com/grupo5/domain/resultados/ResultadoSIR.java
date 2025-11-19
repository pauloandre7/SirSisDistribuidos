package com.grupo5.domain.resultados;

import java.util.List;

/**
 * @author pauloandre7
 * 
 * Classe filha para implementar o atributo que somente a simulação SIR tem: Recuperados
 */
public class ResultadoSIR extends Resultado{

    private List<Double> recuperadosHistorico;

    public ResultadoSIR(double popTotal, double taxaContagio, double taxaRecuperacao, List<Double> tempos, List<Double> sucetiveisHistorico, List<Double> infectadosHistorico, List<Double> recuperadosHistorico) {
        // manda os atributos para a classe mãe
        super(popTotal, taxaContagio, taxaRecuperacao, tempos, sucetiveisHistorico, infectadosHistorico);
        
        this.recuperadosHistorico = recuperadosHistorico;
    }
    
    public List<Double> getRecuperadosHistorico(){
        return recuperadosHistorico;
    }

    public void setRecuperadosHistorico(List<Double> recuperadosHistorico){
        this.recuperadosHistorico = recuperadosHistorico;
    }
}
