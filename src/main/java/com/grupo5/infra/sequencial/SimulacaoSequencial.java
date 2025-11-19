package com.grupo5.infra.sequencial;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import java.io.IOException;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.nonstiff.DormandPrince54Integrator;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

import com.grupo5.application.SimulacaoSIReSIS;
import com.grupo5.application.coletores.SISColetorDeResultados;
import com.grupo5.domain.equacoes.SisEquacoes;
import com.grupo5.domain.resultados.ResultadoSIR;
import com.grupo5.domain.resultados.ResultadoSIS;

/**
 * @author pauloandre7
 * 
 * Fonte da Solução Sequencial (Modelo Matemático e Solver):
 * Utiliza o modelo SIR/SIS clássico de epidemiologia e o solver de EDOs da biblioteca Apache Commons Math.
 * A implementação do modelo matemático é baseada em referências da literatura (ex: Kermack-McKendrick, 1927).
 * O código foi adaptado para a disciplina de Sistemas Distribuídos.
 * 
 * O modelo SIR é para analisar o comportamento de uma epidemia ao longo de um
 * tempo definido pelo analisador. Esse modelo é usado para doenças que só podem
 * ser adquiridas (não sei se é a melhor palavra) uma vez por pessoa.
 * Vale destacar que essa abordagem não trata de pessoas individuais,
 * mas sim de proporções e densidade populacional. Por isso o uso de double.
 * 
 */

public class SimulacaoSequencial {

    private static void simulacaoSIR(){
        
        Scanner scanner = new Scanner(System.in);

        System.out.println("|========================================================|");
        System.out.println("|                SIMULAÇÃO SIR - Parâmetros              |");
        System.out.println("|--------------------------------------------------------|");
        System.out.println("| Valores Numéricos em formato flutuante 00,0            |");
        System.out.println("|========================================================|");
        System.out.print("| População Total: ");
        double popTotal = scanner.nextDouble();
        
        System.out.print("| Infectados Iniciais: ");
        double infectadosInicio = scanner.nextDouble();
                        
        // Determina quantas pessoas um infectado pode infectar antes de se recuperar (ou falecer)
        System.err.print("| Taxa de Contágio da Infecção (0,0 a 10,0): ");
        double taxaContagio = scanner.nextDouble();

        // Determina quanto tempo em média demora para se recuperar
        System.out.print("| Taxa de recuperação dos infectados(0,0 a 2,0): ");
        double taxaRecuperacao = scanner.nextDouble();

        System.out.print("| Tempo inicial: ");
        double tempoInicial = scanner.nextDouble();

        System.out.print("| Tempo limite: ");
        double tempoFinal = scanner.nextDouble();

        System.out.println("|--------------------------------------------------------|");

        // inicia a cronometragem logo após o término das perguntas
        long cronometroInicio = System.currentTimeMillis();

        // Instancia a classe que resolve as simulações dos dois modelos.
        SimulacaoSIReSIS simulacoes = new SimulacaoSIReSIS();
        
        try{
            ResultadoSIR resultadoSIR = simulacoes.simularModeloSIR(popTotal, infectadosInicio, taxaContagio, taxaRecuperacao, tempoInicial, tempoFinal);

            // finaliza a cronometragem logo após a simulação e guarda o tempo de execucao
            long cronometroFinal = System.currentTimeMillis();
            long tempoExecucao = cronometroFinal - cronometroInicio; 
            
            // expõe os resultados no terminal usando o objeto ResultadoSIR.
            System.out.println("|--------------------------------------------------------|");
            System.out.println("| >> Parâmetros da Simulação: ");
            System.out.println("| População: "+popTotal+" \n| Taxa de Contagio: "+taxaContagio+" \n| Taxa de Recuperação:"+taxaRecuperacao);
            System.out.println("|\n| >>  RESULTADOS DA SIMULAÇÃO:");
            System.out.printf("| INICIAL (DIA %.1f) -> S: %.2f, I: %.2f, R: %.2f %n", tempoInicial, 
                                                                                resultadoSIR.getSucetiveisHistorico().getFirst(),
                                                                                resultadoSIR.getInfectadosHistorico().getFirst(), 
                                                                                resultadoSIR.getRecuperadosHistorico().getFirst());
            System.out.printf("| FINAL (DIA %.1f) -> S: %.2f, I: %.2f, R: %.2f %n", tempoFinal, 
                                                                                resultadoSIR.getSucetiveisHistorico().getLast(),
                                                                                resultadoSIR.getInfectadosHistorico().getLast(), 
                                                                                resultadoSIR.getRecuperadosHistorico().getLast());
            System.out.printf("| >> TEMPO DE EXECUCAO: %d ms. %n", tempoExecucao);
            
            // instancia um gráfico usando o XYChart
            XYChart chart = new XYChartBuilder()
                .width(800)
                .height(600)
                .title("Modelo SIR Sequencial")
                .xAxisTitle("Tempo (Dias)")
                .yAxisTitle("População")
            .build();
            
            // adiciona todo o histórico de dados no gráfico
            chart.addSeries("Suscetíveis (S)", resultadoSIR.getTempos(), resultadoSIR.getSucetiveisHistorico());
            chart.addSeries("Infectados (I)", resultadoSIR.getTempos(), resultadoSIR.getInfectadosHistorico());
            chart.addSeries("Recuperados (R)", resultadoSIR.getTempos(), resultadoSIR.getRecuperadosHistorico());

            new SwingWrapper<>(chart).displayChart();
            
        } catch(NumberIsTooSmallException numberExc){
            // Fluxo de erro específico
            System.out.println("|-----------------------------------------------|");
            System.out.println("| >> A taxa de contágio informada é muito alta. |");
            System.out.println("|-----------------------------------------------|");
            numberExc.printStackTrace();
        } catch (Exception e) {
            // Fluxo de erro genérico
            System.out.println("|-----------------------------------------------|");
            System.out.println("|>> Ocorreu um erro inesperado: " + e.getMessage());
            System.out.println("|-----------------------------------------------|");
        }
    }

    private static void simulacaoSIS(){
        
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("|========================================================|");
        System.out.println("|                SIMULAÇÃO SIS - Parâmetros              |");
        System.out.println("|--------------------------------------------------------|");
        System.out.println("| Valores Numéricos em formato flutuante 00,0            |");
        System.out.println("|========================================================|");
        System.out.print("| População Total: ");
        double popTotal = scanner.nextDouble();
        
        System.out.print("| Infectados Iniciais: ");
        double InfectadosInicio = scanner.nextDouble();
        
        System.err.print("| Taxa de Contágio da Infecção (0,0 a 10,0): ");
        double taxaContagio = scanner.nextDouble();

        System.out.print("| Taxa de recuperação dos infectados(0,0 a 2,0): ");
        double taxaRecuperacao = scanner.nextDouble();

        System.out.print("| Tempo inicial: ");
        double tempoInicial = scanner.nextDouble();

        System.out.print("| Tempo limite: ");
        double tempoFinal = scanner.nextDouble();

        System.out.println("|--------------------------------------------------------|");

        long cronometroInicio = System.currentTimeMillis();

        SimulacaoSIReSIS simulacoes = new SimulacaoSIReSIS();

        try{
            ResultadoSIS resultadoSis = simulacoes.simularModeloSIS(popTotal, InfectadosInicio, taxaContagio, taxaRecuperacao, tempoInicial, tempoFinal);
            
            long cronometroFinal = System.currentTimeMillis();
            long tempoExecucao = cronometroFinal - cronometroInicio;

            System.out.println("|--------------------------------------------------------|");
            System.out.println("| PARÂMETROS DA SIMULAÇÃO: ");
            System.out.println("| População: "+popTotal+" \n| Taxa de Contagio: "+taxaContagio+" \n| Taxa de Recuperação:"+taxaRecuperacao);
            System.out.println("|\n| >>  RESULTADOS DA SIMULAÇÃO:");
            System.out.printf("| INICIAL (DIA %.1f) -> S: %.2f, I: %.2f %n", tempoInicial,
                                                                            resultadoSis.getSucetiveisHistorico().getFirst(), 
                                                                            resultadoSis.getInfectadosHistorico().getFirst());
            System.out.printf("| FINAL (DIA %.1f) -> S: %.2f, I: %.2f %n", tempoFinal, 
                                                                            resultadoSis.getSucetiveisHistorico().getLast(), 
                                                                            resultadoSis.getInfectadosHistorico().getLast());
            
            System.out.printf("| >> TEMPO DE EXECUCAO: %d ms. %n", tempoExecucao);

            XYChart chart = new XYChartBuilder()
                .width(800)
                .height(600)
                .title("Modelo SIS Sequencial")
                .xAxisTitle("Tempo (Dias)")
                .yAxisTitle("População")
            .build();
            
            chart.addSeries("Suscetíveis (S)", resultadoSis.getTempos(), resultadoSis.getSucetiveisHistorico());
            chart.addSeries("Infectados (I)", resultadoSis.getTempos(), resultadoSis.getInfectadosHistorico());

            new SwingWrapper<>(chart).displayChart();
            
        } catch(NumberIsTooSmallException numberExc){
            // Fluxo de erro específico
            System.out.println("|-----------------------------------------------|");
            System.out.println("| >> A taxa de contágio informada é muito alta. |");
            System.out.println("|-----------------------------------------------|");
            numberExc.printStackTrace();
        } catch (Exception e) {
            // Fluxo de erro genérico
            System.out.println("|-----------------------------------------------|");
            System.out.println("|>> Ocorreu um erro inesperado: " + e.getMessage());
            System.out.println("|-----------------------------------------------|");
        }
        
    }

    public static void main(String[] args) {
        
        // Scanner para ler as entradas do sistema (system.in)
        Scanner scanner = new Scanner(System.in);
        int escolha = 1;

        while(escolha != 0){
            System.out.println("|========================================================|");
            System.out.println("|            Simulação SIR e SIS - SEQUENCIAL            |");
            System.out.println("|========================================================|");
            System.out.println("| [1] Modelo SIR                                         |");
            System.out.println("| [2] Modelo SIS                                         |");
            System.out.println("| [0] VOLTAR ao Menu Principal                           |");
            System.out.println("|========================================================|");
            System.out.print("| Escolha: ");
            // Recebe a escolha do usuário
            escolha = scanner.nextInt();
            System.out.println("|--------------------------------------------------------|");
            switch (escolha) {
                case 1:
                    simulacaoSIR();
                    break;
                case 2:
                    simulacaoSIS();
                    break;
                case 0:
                    System.out.println("| >> Voltando ao Menu Principal...                       |");
                    System.out.println("|--------------------------------------------------------|");
                    break;
                default:
                    System.out.println("| Opção Inválida!");
            }
        }
    }
}
