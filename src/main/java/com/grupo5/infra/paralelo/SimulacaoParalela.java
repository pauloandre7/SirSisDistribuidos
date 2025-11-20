package com.grupo5.infra.paralelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.grupo5.domain.resultados.Resultado;
import com.grupo5.domain.resultados.ResultadoSIR;
import com.grupo5.domain.resultados.ResultadoSIS;
import com.grupo5.util.ConsoleUtil;
import com.grupo5.util.GraficoUtil;
import com.grupo5.util.TipoModeloEnum;

public class SimulacaoParalela {

    private static void simulacaoPadraoSirOuSis(TipoModeloEnum tipoModelo){
        double popTotal = 1000000.0;
        
        double infectadosInicio = 100.0;

        double taxaContagioLimite = 2.0;

        double taxaRecuperacaoLimite = 0.5;

        double tempoInicial = 0;

        double tempoFinal = 360;

        // Essa diferença garante que o incremento demore 100 passos para essa taxa.
        double diferencaTaxaContagio = 0.01;
        // Essa diferença garante que o incremento demore 50 passos para essa taxa.
        double diferencaTaxaRecuperacao = 0.01;

        // Criar uma Pool de Threads, mas pegando o número de núcleos do PC para otimizar o número de Threads
        // A pool permite uma organização mais eficiente das threads
        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        
        List<Callable<Resultado>> listaDeTarefas = new ArrayList<>();
        
        // Calcula a quantidade de passos para executar até o limite
        int passosBeta = (int) Math.round((taxaContagioLimite / diferencaTaxaContagio));
        int passosGamma = (int) Math.round((taxaRecuperacaoLimite / diferencaTaxaRecuperacao));

        System.out.println("| >> Gerando " + (passosBeta * passosGamma) + " tarefas...");
        
        for (int j = 1; j <= passosGamma; j++) {
            double taxaRecuperacao = j * diferencaTaxaRecuperacao;
            
            for (int i = 1; i <= passosBeta; i++) {
                double taxaContagio = i * diferencaTaxaContagio;

                // cria a lista e guarda todas as tarefas que precisam ser realizadas pelas threads
                SimulacaoTask tarefa = new SimulacaoTask(tipoModelo, popTotal, infectadosInicio, 
                                                         taxaContagio, taxaRecuperacao, 
                                                         tempoInicial, tempoFinal);
                listaDeTarefas.add(tarefa);
            }
        }

        // Fase de execução das tarefas
        try{
            
            System.out.println("\n| >> INICIANDO SIMULAÇÃO");
            long cronometroInicio = System.currentTimeMillis();
            System.out.println("| >> NÚMERO DE THREADS: "+ numThreads);

            // Distribui as tarefas para as threads executarem;
            List<Future<Resultado>> promessas = executor.invokeAll(listaDeTarefas);
            
            List<ResultadoSIR> resultadosSir = new ArrayList<>();
            List<ResultadoSIS> resultadosSis = new ArrayList<>();

            // Percorre todas as promessas e pega cada objeto, que serão alocados na lista correta para suas instâncias
            int contador = 0;
            for (Future<Resultado> futuro : promessas){
                if(futuro.get() instanceof ResultadoSIR){
                    resultadosSir.add((ResultadoSIR) futuro.get());
                    ConsoleUtil.printResutadosSIR(contador, resultadosSir.get(contador), tempoFinal);
                } else {
                    resultadosSis.add((ResultadoSIS) futuro.get());
                    ConsoleUtil.printResutadosSIS(contador, resultadosSis.get(contador), tempoFinal);
                }
                contador++;
            }

            long cronometroFim = System.currentTimeMillis();

            System.out.println("|--------------------------------------------------------|");
            System.out.println("| >> TEMPO DE PROCESSAMENTO PARALELO: "+ (cronometroFim - cronometroInicio) + " ms");

            boolean desejaVerGrafico = true;

                // reseta o contador
                contador = 0;
                
                while(desejaVerGrafico){
                    Scanner scanner = new Scanner(System.in);

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
                            System.out.println("\n|--------------------------------------------------------|");
                            System.out.printf("| DETALHES DA SIMULACAO %d \n", index);
                            
                            if(tipoModelo == TipoModeloEnum.SIR){
                                ConsoleUtil.printResutadosSIR(index, resultadosSir.get(index), tempoFinal);

                                new GraficoUtil().plotarGraficoSIR(resultadosSir.get(index));
                            } else {
                                ConsoleUtil.printResutadosSIS(index, resultadosSis.get(index), tempoFinal);

                                new GraficoUtil().plotarGraficoSIS(resultadosSis.get(index));
                            }
                            
                        } catch(IndexOutOfBoundsException outofbounds){
                            System.out.println("\n|--------------------------------------------------------|");
                            System.out.println("| >> INDEX INCORRETO! Tente novamente ");
                        }
                    }

                    contador++;
                }


        } catch(InterruptedException | ExecutionException e){
            e.printStackTrace();

        } finally {
            // Encerra os trabalhos com esse comando.
            executor.shutdown();
        }
    }

    public static void main(String[] args) {
        
        // Scanner para ler as entradas do sistema (system.in)
        Scanner scanner = new Scanner(System.in);
        int escolha = 1;
        ConsoleUtil.clearConsoleOS();
        while(escolha != 0){
            System.out.println("|========================================================|");
            System.out.println("|             Simulação SIR e SIS - PARALELO             |");
            System.out.println("|========================================================|");
            System.out.println("| [1] Modelo SIR - 10 mil simulações padrões             |");
            System.out.println("| [2] Modelo SIS - 10 mil simulações padrões             |");
            System.out.println("| [0] VOLTAR ao Menu Principal                           |");
            System.out.println("|========================================================|");
            System.out.print("| Escolha: ");
            // Recebe a escolha do usuário
            escolha = scanner.nextInt();
            System.out.println("|--------------------------------------------------------|");
            switch (escolha) {
                case 1:
                    simulacaoPadraoSirOuSis(TipoModeloEnum.SIR);
                    break;
                case 2:
                    simulacaoPadraoSirOuSis(TipoModeloEnum.SIS);
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
