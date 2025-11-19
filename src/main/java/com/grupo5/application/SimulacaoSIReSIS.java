package com.grupo5.application;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.nonstiff.DormandPrince54Integrator;

import com.grupo5.application.coletores.SIRColetorDeResultados;
import com.grupo5.application.coletores.SISColetorDeResultados;
import com.grupo5.domain.equacoes.SirEquacoes;
import com.grupo5.domain.equacoes.SisEquacoes;
import com.grupo5.domain.resultados.ResultadoSIR;
import com.grupo5.domain.resultados.ResultadoSIS;

public class SimulacaoSIReSIS {
    
    public SimulacaoSIReSIS(){

    }

    public ResultadoSIR simularModeloSIR(double popTotal, double infectadosInicio, double taxaContagio, double taxaRecuperacao, double tempoInicial, double tempoFinal){
        
        // Cria a classe de equacoes, mas usando a Interface pra encapsular
        FirstOrderDifferentialEquations equacoesSir = new SirEquacoes(taxaContagio, taxaRecuperacao, popTotal);

        // Instancia um solver da biblioteca (O Runge-Kutta foi o recomendado)
        FirstOrderIntegrator solver = new DormandPrince54Integrator(0.01, 1.0, 1.0e-10, 1.0e-10);

        // O coletor de resultados atua junto com o solver para guardar os resultados de
        // cada salto da simulação
        SIRColetorDeResultados coletor = new SIRColetorDeResultados();
        
        // Adiciona o coletor ao solver
        solver.addStepHandler(coletor);

        // estadoInicial[S, I, R]
        double[] estadoInicial = new double[] {popTotal-infectadosInicio, infectadosInicio, 0.0};

        // Array para guardar o resultado final
        double[] estadoFinal = new double[3]; 

        // Esta é a linha responsavel pela simulaçao toda
        solver.integrate(equacoesSir, tempoInicial, estadoInicial, tempoFinal, estadoFinal);
        
        // retorna a instancia de um modelo de Resultados iniciada com os dados que o coletor armazenou durante a simulacao
        return new ResultadoSIR(coletor.getTempos(), coletor.getSHistorico(), coletor.getIHistorico(), coletor.getRHistorico());
    }

    public ResultadoSIS simularModeloSIS(double popTotal, double infectadosInicio, double taxaContagio, double taxaRecuperacao, double tempoInicial, double tempoFinal){
        // Cria a classe de equacoes
        FirstOrderDifferentialEquations equacoesSis = new SisEquacoes(taxaContagio, taxaRecuperacao, popTotal);

        // Instancia o solver 
        FirstOrderIntegrator solver = new DormandPrince54Integrator(0.01, 1.0, 1.0e-10, 1.0e-10);

        // instancia o coletor
        SISColetorDeResultados coletor = new SISColetorDeResultados();
        
        // Adiciona o coletor ao solver
        solver.addStepHandler(coletor);

        // estadoInicial[S, I, R]
        double[] estadoInicial = new double[] {popTotal-infectadosInicio, infectadosInicio};

        // Array para guardar o resultado final
        double[] estadoFinal = new double[2]; 

        // Linha responsavel pela simulaçao
        solver.integrate(equacoesSis, tempoInicial, estadoInicial, tempoFinal, estadoFinal);
            
        // Cria o objeto de resultado com tudo que o coletor armazenou durante a simulação.
        return new ResultadoSIS(coletor.getTempos(), coletor.getSHistorico(), coletor.getIHistorico());
    }
    
    public List<ResultadoSIR> simularMultiplosModelosSIR(double popTotal, double infectadosInicio, 
        double taxaContagioLimite, double taxaRecuperacaoLimite, double tempoInicial, double tempoFinal, 
        double diferencaTaxaContagio, double diferencaTaxaRecuperacao){
        
        List<ResultadoSIR> resultadosSIR = new ArrayList<>();

        // Variaveis que serão incrementadas a cada ciclo. Começa com o valor do incremento.
        double taxaContagio = diferencaTaxaContagio;
        double taxaRecuperacao = diferencaTaxaRecuperacao;

        // Variaveis para quebrar o while quando as duas taxas chegarem no seus limites
        boolean taxaContagioNoLimite = false;
        boolean taxaRecuperacaoNoLimite = false;
        
        while(true){
            resultadosSIR.add(simularModeloSIR(popTotal, infectadosInicio, taxaContagio, taxaRecuperacao, tempoInicial, tempoFinal));
            
            taxaContagio += diferencaTaxaContagio;
            taxaRecuperacao += diferencaTaxaRecuperacao;

            if(taxaContagio > taxaContagioLimite){
                taxaContagio = taxaContagioLimite;
                taxaContagioNoLimite = true;    
            }
            if(taxaRecuperacao > taxaRecuperacaoLimite){
                taxaRecuperacao = taxaRecuperacaoLimite;
                taxaRecuperacaoNoLimite = true;
            }
            
            if(taxaContagioNoLimite && taxaRecuperacaoNoLimite) {
                break; 
            }
        }

        return resultadosSIR;
    }

    public List<ResultadoSIS> simularMultiplosModelosSIS(double popTotal, double infectadosInicio, 
        double taxaContagioLimite, double taxaRecuperacaoLimite, double tempoInicial, double tempoFinal, 
        double diferencaTaxaContagio, double diferencaTaxaRecuperacao){
        
        List<ResultadoSIS> resultadosSIS = new ArrayList<>();

        double taxaContagio = diferencaTaxaContagio;
        double taxaRecuperacao = diferencaTaxaRecuperacao;

        boolean taxaContagioNoLimite = false;
        boolean taxaRecuperacaoNoLimite = false;
        
        while(true){
            resultadosSIS.add(simularModeloSIS(popTotal, infectadosInicio, taxaContagio, taxaRecuperacao, tempoInicial, tempoFinal));
            
            taxaContagio += diferencaTaxaContagio;
            taxaRecuperacao += diferencaTaxaRecuperacao;

            if(taxaContagio > taxaContagioLimite){
                taxaContagio = taxaContagioLimite;
                taxaContagioNoLimite = true;    
            }
            if(taxaRecuperacao > taxaRecuperacaoLimite){
                taxaRecuperacao = taxaRecuperacaoLimite;
                taxaRecuperacaoNoLimite = true;
            }
            
            if(taxaContagioNoLimite && taxaRecuperacaoNoLimite) {
                break; 
            }
        }

        return resultadosSIS;
    }
}
