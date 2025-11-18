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

import com.grupo5.application.coletores.SIRColetorDeResultados;
import com.grupo5.application.coletores.SISColetorDeResultados;
import com.grupo5.domain.equacoes.SirEquacoes;
import com.grupo5.domain.equacoes.SisEquacoes;

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

public class ModelosSequenciais {
    
    public static void clearConsoleOS() {
        try {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows")) {
                // Executa "cmd /c cls" no Windows
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                // Executa "clear" no Linux/Mac
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (final IOException | InterruptedException e) {
            e.printStackTrace(); 
        }
    }

    private static void simulacaoSIR(){
        
        Scanner scanner = new Scanner(System.in);
        
        // Limpa o terminal com base no console
        clearConsoleOS();
        
        System.out.println("|========================================================|");
        System.out.println("|                SIMULAÇÃO SIR - Parâmetros              |");
        System.out.println("|--------------------------------------------------------|");
        System.out.println("| Valores Numéricos em formato flutuante 00,0            |");
        System.out.println("|========================================================|");
        System.out.print("| População Total: ");
        double popTotal = scanner.nextDouble();
        
        System.out.print("| Infectados Iniciais: ");
        double InfectadosInicio = scanner.nextDouble();
                        
        // estadoInicial[S, I, R]
        double[] estadoInicial = new double[] {popTotal-InfectadosInicio, InfectadosInicio, 0.0};

        // Determina quantas pessoas um infectado pode infectar antes de se recuperar (ou falecer)
        System.err.print("| Taxa de Contágio da Infecção (0,0 a 1,0): ");
        double taxaContagio = scanner.nextDouble();

        // Determina quanto tempo em média demora para se recuperar
        System.out.print("| Taxa de recuperação dos infectados(0,0 a 1,0): ");
        double taxaRecuperacao = scanner.nextDouble();

        System.out.print("| Tempo inicial: ");
        double tempoInicial = scanner.nextDouble();

        System.out.print("| Tempo limite: ");
        double tempoFinal = scanner.nextDouble();

        System.out.println("|--------------------------------------------------------|");

        // PARA REFATOR: posso colocar toda a parte de calculo em uma nova classe que fica apenas responsavel por isso
        // -------- Parte do cálculo --------
        // Cria a classe de equacoes, mas usando a Interface pra encapsular
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

        try{
            // Esta é a linha responsavel pela simulaçao toda
            solver.integrate(equacoesSir, tempoInicial, estadoInicial, tempoFinal, estadoFinal);
        } catch(NumberIsTooSmallException numberExc){
            System.out.println("-----------------------------------------------");
            System.out.println(">> A taxa de contágio informada é muito alta.");
            System.out.println("-----------------------------------------------");
            numberExc.printStackTrace();

        }
        // PARA REFATOR: Posso criar um modelo ResultadoSIR e colocar os históricos nela
        // Recupera todas as séries de dados
        List<Double> tempos = coletor.getTempos();
        List<Double> sucetiveisHistorico = coletor.getSHistorico();
        List<Double> infectadosHistorico = coletor.getIHistorico();
        List<Double> recuperadosHistorico = coletor.getRHistorico();

        // Ao final, estadoFinal conterá os valores de S, I, R no tempo 100.
        System.out.println("|--------------------------------------------------------|");
        System.out.println("| >> Parâmetros da Simulação: ");
        System.out.println("| População: "+popTotal+" \n| Taxa de Contagio: "+taxaContagio+" \n| Taxa de Recuperação:"+taxaRecuperacao);
        System.out.println("|\n| >>  RESULTADOS DA SIMULAÇÃO:");
        System.out.printf("| INICIAL (DIA %.1f) -> S: %.2f, I: %.2f, R: %.2f%n", tempoInicial, 
                                                                            sucetiveisHistorico.get(0), 
                                                                            infectadosHistorico.get(0), 
                                                                            recuperadosHistorico.get(0));
        System.out.printf("| FINAL (DIA %.1f) -> S: %.2f, I: %.2f, R: %.2f%n", tempoFinal, 
                                                                                estadoFinal[0], 
                                                                                estadoFinal[1], 
                                                                                estadoFinal[2]);
        
        // instancia um gráfico usando o XYChart
        XYChart chart = new XYChartBuilder()
            .width(800)
            .height(600)
            .title("Modelo SIR Sequencial")
            .xAxisTitle("Tempo (Dias)")
            .yAxisTitle("População")
        .build();
        
        // adiciona todo o histórico de dados no gráfico
        chart.addSeries("Suscetíveis (S)", tempos, sucetiveisHistorico);
        chart.addSeries("Infectados (I)", tempos, infectadosHistorico);
        chart.addSeries("Recuperados (R)", tempos, recuperadosHistorico);

        new SwingWrapper<>(chart).displayChart();
    }

    private static void simulacaoSIS(){
        
        Scanner scanner = new Scanner(System.in);
        
        // Limpa o terminal com base no console
        clearConsoleOS();
        
        System.out.println("|========================================================|");
        System.out.println("|                SIMULAÇÃO SIS - Parâmetros              |");
        System.out.println("|--------------------------------------------------------|");
        System.out.println("| Valores Numéricos em formato flutuante 00,0            |");
        System.out.println("|========================================================|");
        System.out.print("| População Total: ");
        double popTotal = scanner.nextDouble();
        
        System.out.print("| Infectados Iniciais: ");
        double InfectadosInicio = scanner.nextDouble();
                        
        // estadoInicial[S, I, R]
        double[] estadoInicial = new double[] {popTotal-InfectadosInicio, InfectadosInicio};

        // Determina quantas pessoas um infectado pode infectar antes de se recuperar (ou falecer)
        System.err.print("| Taxa de Contágio da Infecção (0,0 a 1,0): ");
        double taxaContagio = scanner.nextDouble();

        // Determina quanto tempo em média demora para se recuperar
        System.out.print("| Taxa de recuperação dos infectados(0,0 a 1,0): ");
        double taxaRecuperacao = scanner.nextDouble();

        System.out.print("| Tempo inicial: ");
        double tempoInicial = scanner.nextDouble();

        System.out.print("| Tempo limite: ");
        double tempoFinal = scanner.nextDouble();

        System.out.println("|--------------------------------------------------------|");

        // -------- Parte do cálculo --------
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

        try{
            // Linha responsavel pela simulaçao
            solver.integrate(equacoesSis, tempoInicial, estadoInicial, tempoFinal, estadoFinal);
        } catch(NumberIsTooSmallException numberExc){
            System.out.println("|-----------------------------------------------|");
            System.out.println("| >> A taxa de contágio informada é muito alta. |");
            System.out.println("|-----------------------------------------------|");
            numberExc.printStackTrace();

        }
        
        // Recupera todas as séries de dados
        List<Double> tempos = coletor.getTempos();
        List<Double> sucetiveisHistorico = coletor.getSHistorico();
        List<Double> infectadosHistorico = coletor.getIHistorico();

        // Ao final, estadoFinal conterá os valores de S, I, R no tempo 100.
        System.out.println("|--------------------------------------------------------|");
        System.out.println("| PARÂMETROS DA SIMULAÇÃO: ");
        System.out.println("| População: "+popTotal+" \n| Taxa de Contagio: "+taxaContagio+" \n| Taxa de Recuperação:"+taxaRecuperacao);
        System.out.println("|\n| >>  RESULTADOS DA SIMULAÇÃO:");
        System.out.printf("| INICIAL (DIA %.1f) -> S: %.2f, I: %.2f \n", tempoInicial, 
                                                                            sucetiveisHistorico.get(0), 
                                                                            infectadosHistorico.get(0));
        System.out.printf("| FINAL (DIA %.1f) -> S: %.2f, I: %.2f \n", tempoFinal, 
                                                                                estadoFinal[0], 
                                                                                estadoFinal[1]);
        
        // instancia um gráfico usando o XYChart
        XYChart chart = new XYChartBuilder()
            .width(800)
            .height(600)
            .title("Modelo SIS Sequencial")
            .xAxisTitle("Tempo (Dias)")
            .yAxisTitle("População")
        .build();
        
        // adiciona todo o histórico de dados no gráfico
        chart.addSeries("Suscetíveis (S)", tempos, sucetiveisHistorico);
        chart.addSeries("Infectados (I)", tempos, infectadosHistorico);

        new SwingWrapper<>(chart).displayChart();
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
