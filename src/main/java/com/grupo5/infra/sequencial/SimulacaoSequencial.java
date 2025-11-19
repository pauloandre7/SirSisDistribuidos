package com.grupo5.infra.sequencial;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

import com.grupo5.application.SimulacaoSIReSIS;
import com.grupo5.domain.resultados.ResultadoSIR;
import com.grupo5.domain.resultados.ResultadoSIS;
import com.grupo5.util.GraficoUtil;

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
        System.out.print("| Taxa de Contágio da Infecção (0,0 a 10,0): ");
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
            System.out.printf("|    - População: %.2f \n", popTotal);
            System.out.printf("|    - Taxa de Contagio: %.2f \n", taxaContagio);
            System.out.printf("|    - Taxa de Recuperação: %.2f \n", taxaRecuperacao);
            System.out.println("| >>  RESULTADOS DA SIMULAÇÃO:");
            System.out.printf("|    - INICIAL (DIA %.1f) -> S: %.2f, I: %.2f, R: %.2f %n", tempoInicial, 
                                                                                resultadoSIR.getSucetiveisHistorico().getFirst(),
                                                                                resultadoSIR.getInfectadosHistorico().getFirst(), 
                                                                                resultadoSIR.getRecuperadosHistorico().getFirst());
            System.out.printf("|    - FINAL (DIA %.1f) -> S: %.2f, I: %.2f, R: %.2f %n", tempoFinal, 
                                                                                resultadoSIR.getSucetiveisHistorico().getLast(),
                                                                                resultadoSIR.getInfectadosHistorico().getLast(), 
                                                                                resultadoSIR.getRecuperadosHistorico().getLast());
            System.out.printf("| >> TEMPO DE EXECUCAO: %d ms. %n", tempoExecucao);
            
            new GraficoUtil().plotarGraficoSIR(resultadoSIR);
            
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
        double infectadosInicio = scanner.nextDouble();
        
        System.out.print("| Taxa de Contágio da Infecção (0,0 a 10,0): ");
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
            ResultadoSIS resultadoSis = simulacoes.simularModeloSIS(popTotal, infectadosInicio, taxaContagio, taxaRecuperacao, tempoInicial, tempoFinal);
            
            long cronometroFinal = System.currentTimeMillis();
            long tempoExecucao = cronometroFinal - cronometroInicio;

            System.out.println("|--------------------------------------------------------|");
            System.out.println("| PARÂMETROS DA SIMULAÇÃO: ");
            System.out.printf("|    - População: %.2f \n", popTotal);
            System.out.printf("|    - Taxa de Contagio: %.2f \n", taxaContagio);
            System.out.printf("|    - Taxa de Recuperação: %.2f \n", taxaRecuperacao);
            System.out.println("| >> RESULTADOS DA SIMULAÇÃO:");
            System.out.printf("|    - INICIAL (DIA %.1f) -> S: %.2f, I: %.2f %n", tempoInicial,
                                                                            resultadoSis.getSucetiveisHistorico().getFirst(), 
                                                                            resultadoSis.getInfectadosHistorico().getFirst());
            System.out.printf("|    - FINAL (DIA %.1f) -> S: %.2f, I: %.2f %n", tempoFinal, 
                                                                            resultadoSis.getSucetiveisHistorico().getLast(), 
                                                                            resultadoSis.getInfectadosHistorico().getLast());
            
            System.out.printf("| >> TEMPO DE EXECUCAO: %d ms. %n", tempoExecucao);

            new GraficoUtil().plotarGraficoSIS(resultadoSis);
            
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

    private static void simulacaoMultipla(int sirOuSis){
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("|=============================================================|");
        if(sirOuSis == 3){
            System.out.println("|             SIMULAÇÃO MÚLTIPLA SIR - Parâmetros             |");
        }else{
            System.out.println("|             SIMULAÇÃO MÚLTIPLA SIS - Parâmetros             |");
        }
        System.out.println("|-------------------------------------------------------------|");
        System.out.println("| Valores Numéricos em formato flutuante 00,0                 |");
        System.out.println("|=============================================================|");
        System.out.print("| População Total: ");
        double popTotal = scanner.nextDouble();
        
        System.out.print("| Infectados Iniciais: ");
        double infectadosInicio = scanner.nextDouble();

        System.out.println("|-------------------------- AVISO 1 --------------------------|");
        System.out.println("| >> A simulação múltipla irá realizar inúmeras simulações");
        System.out.println("| >> até atingir o valor limite informado nas taxas abaixo");
        System.out.println("|-------------------------------------------------------------|");

        System.out.print("| Limite da taxa de Contágio da Infecção (0,0 a 10,0): ");
        double taxaContagioLimite = scanner.nextDouble();

        System.out.print("| Limite da taxa de recuperação dos infectados(0,0 a 2,0): ");
        double taxaRecuperacaoLimite = scanner.nextDouble();

        System.out.print("| Tempo inicial: ");
        double tempoInicial = scanner.nextDouble();

        System.out.print("| Tempo limite: ");
        double tempoFinal = scanner.nextDouble();

        System.out.println("|-------------------------- AVISO 2 --------------------------|");
        System.out.println("| >> A diferença informada abaixo determinará a quantidade de");
        System.out.println("| >> simulações feitas. Será o valor que será acrescido às ");
        System.out.println("| >> taxas após o término de cada simulação. ");
        System.out.println("| >> ATENÇÃO: valores muito pequenos geram muitas simulações! ");
        System.out.println("|-------------------------------------------------------------|");

        System.out.print("| Diferença de cada taxa de contágio (0,1 a 10,0): ");
        double diferencaTaxaContagio = scanner.nextDouble();

        System.out.print("| Diferença de cada taxa de contágio (0,1 a 2,0): ");
        double diferencaTaxaRecuperacao = scanner.nextDouble();

        System.out.println("\n| >> INICIANDO SIMULAÇÃO");
        long cronometroInicio = System.currentTimeMillis();

        // Variaveis que serão incrementadas a cada ciclo. Começa com o valor do incremento.
        double taxaContagio = diferencaTaxaContagio;
        double taxaRecuperacao = diferencaTaxaRecuperacao;
        
        int contador = 0;

        if(sirOuSis == 3){
            SimulacaoSIReSIS simulacoes = new SimulacaoSIReSIS();
        
            try{
                // Adiciona o resultado da simulação na lista
                List<ResultadoSIR> resultadosSIR = simulacoes.simularMultiplosModelosSIR(popTotal, infectadosInicio, taxaContagioLimite, 
                        taxaRecuperacaoLimite, tempoInicial, tempoFinal, diferencaTaxaContagio, diferencaTaxaRecuperacao);
                    
                for(ResultadoSIR resultadoSIR : resultadosSIR){
                    // expõe os resultados no terminal usando o objeto ResultadoSIR.
                    System.out.println("|--------------------------------------------------------|");
                    System.out.printf("| >> PARÂMETROS DA SIMULAÇÃO %d: %n", contador);
                    System.out.printf("|    - População: %.2f \n", popTotal);
                    System.out.printf("|    - Taxa de Contagio: %.2f \n", taxaContagio);
                    System.out.printf("|    - Taxa de Recuperação: %.2f \n", taxaRecuperacao);
                    System.out.printf("| >> RESULTADOS DA SIMULAÇÃO %d: %n", contador);
                    System.out.printf("|    - DIA %.1f -> S: %.2f, I: %.2f, R: %.2f %n", tempoFinal, 
                                                                                    resultadoSIR.getSucetiveisHistorico().getLast(),
                                                                                    resultadoSIR.getInfectadosHistorico().getLast(), 
                                                                                    resultadoSIR.getRecuperadosHistorico().getLast()
                    );
                    
                    // Contadores parecidos com os que estão na classe que realiza simulações
                    // Apenas para informar corretamente as taxas no terminal.
                    taxaContagio += diferencaTaxaContagio;
                    taxaRecuperacao += diferencaTaxaRecuperacao;

                    if(taxaContagio > taxaContagioLimite){
                        taxaContagio = taxaContagioLimite;
                    }
                    if(taxaRecuperacao > taxaRecuperacaoLimite){
                        taxaRecuperacao = taxaRecuperacaoLimite;
                    }
                    
                    contador++;
                }
                // finaliza a cronometragem logo após a simulação e guarda o tempo de execucao
                long cronometroFinal = System.currentTimeMillis();
                long tempoExecucao = cronometroFinal - cronometroInicio; 
                System.out.println("|--------------------------------------------------------|");
                System.out.printf("| >> TEMPO DE EXECUÇÃO TOTAL: %d ms. %n", tempoExecucao);

                boolean desejaVerGrafico = true;

                contador = 0;
                while(desejaVerGrafico){
                    // consome o \n que ficou no buffer por causa de outras leitura acima e prepara o scanner para outra leitura.
                    scanner.nextLine();

                    System.out.println("|--------------------------------------------------------|");
                    if (contador == 0){
                        System.out.printf("| >> DESEJA VER O GRÁFICO DE ALGUMA SIMULAÇÃO (s/n)? ");
                    } else {
                        System.out.printf("| >> DESEJA VER O GRÁFICO DE OUTRA SIMULAÇÃO (s/n)? ");
                    }
                    String resp = scanner.nextLine();
                    
                    // Se a resposta for 'n', então...
                    if (resp.toLowerCase().equals("n")) desejaVerGrafico = false;

                    if(desejaVerGrafico){
                        int index = 0;
                        System.out.println("\n|--------------------------------------------------------|");
                        System.out.printf("| >> Index da simulação: ");
                        index = scanner.nextInt();

                        try{
                            new GraficoUtil().plotarGraficoSIR(resultadosSIR.get(index));
                        } catch(IndexOutOfBoundsException outofbounds){
                            System.out.println("\n|--------------------------------------------------------|");
                            System.out.println("| >> INDEX INCORRETO! Tente novamente ");
                        }
                    }

                    contador++;
                }

            } catch(NumberIsTooSmallException numberExc){
                System.out.println("|-----------------------------------------------|");
                System.out.println("| >> A taxa de contágio informada é muito alta. |");
                System.out.println("|-----------------------------------------------|");
                numberExc.printStackTrace();
            } catch (Exception e) {
                System.out.println("|-----------------------------------------------|");
                System.out.println("|>> Ocorreu um erro inesperado: " + e.getMessage());
                System.out.println("|-----------------------------------------------|");
            }
        } else {
            SimulacaoSIReSIS simulacoes = new SimulacaoSIReSIS();
        
            try{
                List<ResultadoSIS> resultadosSIS = simulacoes.simularMultiplosModelosSIS(popTotal, infectadosInicio, taxaContagioLimite, 
                    taxaRecuperacaoLimite, tempoInicial, tempoFinal, diferencaTaxaContagio, diferencaTaxaRecuperacao);

                contador = 0;
                for(ResultadoSIS resultadoSIS : resultadosSIS){
                    System.out.println("|--------------------------------------------------------|");
                    System.out.printf("| >> PARÂMETROS DA SIMULAÇÃO %d: %n", contador);
                    System.out.printf("|    - População: %.2f \n", popTotal);
                    System.out.printf("|    - Taxa de Contagio: %.2f \n", taxaContagio);
                    System.out.printf("|    - Taxa de Recuperação: %.2f \n", taxaRecuperacao);
                    System.out.printf("| >> RESULTADOS DA SIMULAÇÃO %d: %n", contador);
                    System.out.printf("|    - DIA %.1f -> S: %.2f, I: %.2f %n", tempoFinal, 
                                                                            resultadoSIS.getSucetiveisHistorico().getLast(),
                                                                            resultadoSIS.getInfectadosHistorico().getLast()
                    );
                    
                    // Contadores parecidos com os que estão na classe que realiza simulações
                    // Apenas para informar corretamente as taxas no terminal.
                    taxaContagio += diferencaTaxaContagio;
                    taxaRecuperacao += diferencaTaxaRecuperacao;

                    if(taxaContagio > taxaContagioLimite){
                        taxaContagio = taxaContagioLimite;
                    }
                    if(taxaRecuperacao > taxaRecuperacaoLimite){
                        taxaRecuperacao = taxaRecuperacaoLimite;
                    }
                    
                    contador++;
                }
                
                long cronometroFinal = System.currentTimeMillis();
                long tempoExecucao = cronometroFinal - cronometroInicio; 
                System.out.println("|--------------------------------------------------------|");
                System.out.printf("| >> TEMPO DE EXECUÇÃO TOTAL: %d ms. %n", tempoExecucao);
                
                boolean desejaVerGrafico = true;

                contador = 0;
                while(desejaVerGrafico){
                    scanner.nextLine();

                    System.out.println("|--------------------------------------------------------|");
                    if (contador == 0){
                        System.out.print("| >> DESEJA VER O GRÁFICO DE ALGUMA SIMULAÇÃO (s/n)? ");
                    } else {
                        System.out.print("| >> DESEJA VER O GRÁFICO DE OUTRA SIMULAÇÃO (s/n)? ");
                    }
                    String resp = scanner.nextLine();
                    
                    // Se a resposta for 'n', então...
                    if (resp.toLowerCase().equals("n")) desejaVerGrafico = false;

                    if(desejaVerGrafico){
                        int index = 0;
                        System.out.println("\n|--------------------------------------------------------|");
                        System.out.print("| >> Index da simulação: ");
                        index = scanner.nextInt();

                        
                        GraficoUtil graficoUtil = new GraficoUtil();
                        try{
                            graficoUtil.plotarGraficoSIS(resultadosSIS.get(index));
                        } catch(IndexOutOfBoundsException outofbounds){
                            System.out.println("\n|--------------------------------------------------------|");
                            System.out.println("| >> INDEX INCORRETO! Tente novamente ");
                        }
                    }

                    contador++;
                }

            } catch(NumberIsTooSmallException numberExc){
                System.out.println("|-----------------------------------------------|");
                System.out.println("| >> A taxa de contágio informada é muito alta. |");
                System.out.println("|-----------------------------------------------|");
                numberExc.printStackTrace();
            } catch (Exception e) {
                System.out.println("|-----------------------------------------------|");
                System.out.println("|>> Ocorreu um erro inesperado: " + e.getMessage());
                System.out.println("|-----------------------------------------------|");
            }
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
            System.out.println("| [3] Simulações Múltiplas - Modelo SIR                  |");
            System.out.println("| [4] Simulações Múltiplas - Modelo SIS                  |");
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
                case 3:
                    simulacaoMultipla(3);
                    break;
                case 4:
                    simulacaoMultipla(4);
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
