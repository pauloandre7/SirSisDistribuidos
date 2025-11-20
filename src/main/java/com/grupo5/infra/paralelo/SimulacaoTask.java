package com.grupo5.infra.paralelo;

import java.util.concurrent.Callable;

import com.grupo5.application.SimulacaoSIReSIS;
import com.grupo5.domain.resultados.Resultado;
import com.grupo5.util.TipoModeloEnum;

/**
 * @author pauloandre7
 * 
 * Essa classe implementa a task que a thread precisa fazer. Implementa Callable porque precisa retornar um objeto
 */
public class SimulacaoTask implements Callable<Resultado>{
    // Parâmetros de UMA simulação específica
    private TipoModeloEnum tipo; // Define se vai rodar SIR ou SIS
    private double popTotal;
    private double infectadosInicio;
    private double taxaContagio;
    private double taxaRecuperacao;
    private double tempoInicial;
    private double tempoFinal;

    public SimulacaoTask(TipoModeloEnum tipo, double popTotal, double infectadosInicio, double taxaContagio, 
                         double taxaRecuperacao, double tempoInicial, double tempoFinal) {
        this.tipo = tipo;
        this.popTotal = popTotal;
        this.infectadosInicio = infectadosInicio;
        this.taxaContagio = taxaContagio;
        this.taxaRecuperacao = taxaRecuperacao;
        this.tempoInicial = tempoInicial;
        this.tempoFinal = tempoFinal;
    }
    
    /**
     * @return Objeto Resultado, podendo ser um ResultadoSIR ou ResultadoSIS
     * 
     * Usa a herança para evitar a implementação de duas tasks para cada Modelo.
     */
    @Override
    public Resultado call() throws Exception {
        // Instanciar o service aqui garante que não haja conflito de memória
        SimulacaoSIReSIS service = new SimulacaoSIReSIS();

        if (this.tipo == TipoModeloEnum.SIR) {
            return service.simularModeloSIR(popTotal, infectadosInicio, taxaContagio, 
                                            taxaRecuperacao, tempoInicial, tempoFinal);
        } else {
            return service.simularModeloSIS(popTotal, infectadosInicio, taxaContagio, 
                                            taxaRecuperacao, tempoInicial, tempoFinal);
        }
    }
}
