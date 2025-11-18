package com.pauloandre7.distribuido;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.nonstiff.DormandPrince54Integrator;

import com.pauloandre7.coletores.SIRColetorDeResultados;
import com.pauloandre7.coletores.SISColetorDeResultados;
import com.pauloandre7.equacoes.SirEquacoes;
import com.pauloandre7.equacoes.SisEquacoes;

/**
 * Implementação da Interface Remota ISimulacaoRemota.
 * Contém a lógica de simulação que será executada no servidor.
 * 
 * Fonte: Adaptado do exemplo de RMI fornecido pelo professor (AulaRMI(1).zip)
 */
public class SimulacaoRemotaImpl extends UnicastRemoteObject implements ISimulacaoRemota {

    // Construtor obrigatório para a classe que estende UnicastRemoteObject
    public SimulacaoRemotaImpl() throws RemoteException {
        super();
    }

    /**
     * Método auxiliar para executar a simulação SIR.
     * @param popTotal População total.
     * @param infectadosInicio Número inicial de infectados.
     * @param taxaContagio Taxa de contágio.
     * @param taxaRecuperacao Taxa de recuperação.
     * @param tempoInicial Tempo inicial da simulação.
     * @param tempoFinal Tempo final da simulação.
     * @return Uma lista de listas de Double contendo os resultados: [Tempos, Suscetíveis, Infectados, Recuperados].
     * @throws RemoteException
     */
    @Override
    public List<List<Double>> simularSIR(double popTotal, double infectadosInicio, double taxaContagio, double taxaRecuperacao, double tempoInicial, double tempoFinal) throws RemoteException {
        
        // estadoInicial[S, I, R]
        double[] estadoInicial = new double[] {popTotal - infectadosInicio, infectadosInicio, 0.0};

        // Cria a classe de equacoes
        FirstOrderDifferentialEquations equacoesSir = new SirEquacoes(taxaContagio, taxaRecuperacao, popTotal);

        // Instancia um solver da biblioteca (O Runge-Kutta foi o recomendado)
        FirstOrderIntegrator solver = new DormandPrince54Integrator(0.01, 1.0, 1.0e-10, 1.0e-10);

        // O coletor de resultados atua junto com o solver para guardar os resultados de
        // cada salto da simulação
        SIRColetorDeResultados coletor = new SIRColetorDeResultados();
        
        // Adiciona o coletor ao solver
        solver.addStepHandler(coletor);

        // Array para guardar o resultado final
        double[] estadoFinal = new double[3]; 

        // Esta é a linha responsavel pela simulaçao toda
        solver.integrate(equacoesSir, tempoInicial, estadoInicial, tempoFinal, estadoFinal);
        
        // Recupera todas as séries de dados
        List<Double> tempos = coletor.getTempos();
        List<Double> sucetiveisHistorico = coletor.getSHistorico();
        List<Double> infectadosHistorico = coletor.getIHistorico();
        List<Double> recuperadosHistorico = coletor.getRHistorico();

        List<List<Double>> resultados = new ArrayList<>();
        resultados.add(tempos);
        resultados.add(sucetiveisHistorico);
        resultados.add(infectadosHistorico);
        resultados.add(recuperadosHistorico);
        
        return resultados;
    }

    /**
     * Método auxiliar para executar a simulação SIS.
     * @param popTotal População total.
     * @param infectadosInicio Número inicial de infectados.
     * @param taxaContagio Taxa de contágio.
     * @param taxaRecuperacao Taxa de recuperação.
     * @param tempoInicial Tempo inicial da simulação.
     * @param tempoFinal Tempo final da simulação.
     * @return Uma lista de listas de Double contendo os resultados: [Tempos, Suscetíveis, Infectados].
     * @throws RemoteException
     */
    @Override
    public List<List<Double>> simularSIS(double popTotal, double infectadosInicio, double taxaContagio, double taxaRecuperacao, double tempoInicial, double tempoFinal) throws RemoteException {
        
        // estadoInicial[S, I]
        double[] estadoInicial = new double[] {popTotal - infectadosInicio, infectadosInicio};

        // Cria a classe de equacoes
        FirstOrderDifferentialEquations equacoesSis = new SisEquacoes(taxaContagio, taxaRecuperacao, popTotal);

        // Instancia o solver 
        FirstOrderIntegrator solver = new DormandPrince54Integrator(0.01, 1.0, 1.0e-10, 1.0e-10);

        // instancia o coletor
        SISColetorDeResultados coletor = new SISColetorDeResultados();
        
        // Adiciona o coletor ao solver
        solver.addStepHandler(coletor);

        // Array para guardar o resultado final
        double[] estadoFinal = new double[2]; 

        // Linha responsavel pela simulaçao
        solver.integrate(equacoesSis, tempoInicial, estadoInicial, tempoFinal, estadoFinal);
        
        // Recupera todas as séries de dados
        List<Double> tempos = coletor.getTempos();
        List<Double> sucetiveisHistorico = coletor.getSHistorico();
        List<Double> infectadosHistorico = coletor.getIHistorico();

        List<List<Double>> resultados = new ArrayList<>();
        resultados.add(tempos);
        resultados.add(sucetiveisHistorico);
        resultados.add(infectadosHistorico);
        
        return resultados;
    }
}
