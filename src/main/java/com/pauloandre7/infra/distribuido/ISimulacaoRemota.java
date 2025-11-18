package com.pauloandre7.infra.distribuido;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Interface Remota para a Simulação Distribuída dos Modelos SIR/SIS.
 * 
 * Fonte: Adaptado do exemplo de RMI fornecido pelo professor (AulaRMI(1).zip)
 */
public interface ISimulacaoRemota extends Remote {
    
    /**
     * Executa a simulação SIR de forma remota.
     * @param popTotal População total.
     * @param infectadosInicio Número inicial de infectados.
     * @param taxaContagio Taxa de contágio.
     * @param taxaRecuperacao Taxa de recuperação.
     * @param tempoInicial Tempo inicial da simulação.
     * @param tempoFinal Tempo final da simulação.
     * @return Uma lista de listas de Double contendo os resultados: [Tempos, Suscetíveis, Infectados, Recuperados].
     * @throws RemoteException
     */
    List<List<Double>> simularSIR(double popTotal, double infectadosInicio, double taxaContagio, double taxaRecuperacao, double tempoInicial, double tempoFinal) throws RemoteException;

    /**
     * Executa a simulação SIS de forma remota.
     * @param popTotal População total.
     * @param infectadosInicio Número inicial de infectados.
     * @param taxaContagio Taxa de contágio.
     * @param taxaRecuperacao Taxa de recuperação.
     * @param tempoInicial Tempo inicial da simulação.
     * @param tempoFinal Tempo final da simulação.
     * @return Uma lista de listas de Double contendo os resultados: [Tempos, Suscetíveis, Infectados].
     * @throws RemoteException
     */
    List<List<Double>> simularSIS(double popTotal, double infectadosInicio, double taxaContagio, double taxaRecuperacao, double tempoInicial, double tempoFinal) throws RemoteException;
}
