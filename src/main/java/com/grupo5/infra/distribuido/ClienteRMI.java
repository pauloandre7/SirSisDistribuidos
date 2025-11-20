package com.grupo5.infra.distribuido;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.math3.exception.NumberIsTooSmallException;

import com.grupo5.domain.resultados.ResultadoSIR;
import com.grupo5.domain.resultados.ResultadoSIS;
import com.grupo5.util.ConsoleUtil;
import com.grupo5.util.GraficoUtil;

/**
 * Classe principal para o Cliente RMI.
 * 
 * Fonte: Adaptado do exemplo de RMI fornecido pelo professor (AulaRMI(1).zip)
 */
public class ClienteRMI {
    
    private static void simulacaoTesteSimplesSIReSIS(ISimulacaoRemota stub){
        // 3. Chama o método remoto (Simulação SIR com dados de exemplo)
            double popTotal = 1000.0;
            double infectadosInicio = 10.0;
            double taxaContagio = 0.3;
            double taxaRecuperacao = 0.1;
            double tempoInicial = 0.0;
            double tempoFinal = 100.0;
            
            try{

                List<List<Double>> resultadosSIR = stub.simularSIR(popTotal, infectadosInicio, taxaContagio, taxaRecuperacao, tempoInicial, tempoFinal);
                
                System.out.println("| Simulação SIR remota concluída. Número de pontos de dados: " + resultadosSIR.get(0).size());
                System.out.printf("| Exemplo de resultado final (S, I, R): %.2f, %.2f, %.2f \n", 
                                    resultadosSIR.get(1).get(resultadosSIR.get(0).size() - 1),
                                    resultadosSIR.get(2).get(resultadosSIR.get(0).size() - 1),
                                    resultadosSIR.get(3).get(resultadosSIR.get(0).size() - 1));

                System.out.println("\n| >> Executando simulação SIS remota...");

                // 4. Chama o método remoto (Simulação SIS com dados de exemplo)
                List<List<Double>> resultadosSIS = stub.simularSIS(popTotal, infectadosInicio, taxaContagio, taxaRecuperacao, tempoInicial, tempoFinal);

                System.out.println("| Simulação SIS remota concluída. Número de pontos de dados: " + resultadosSIS.get(0).size());
                System.out.printf("| Exemplo de resultado final (S, I): %.2f, %.2f \n",
                                    resultadosSIS.get(1).get(resultadosSIS.get(0).size() - 1),
                                    resultadosSIS.get(2).get(resultadosSIS.get(0).size() - 1));

            } catch(RemoteException e){
                System.err.println("| >> Erro no Cliente RMI: " + e.toString());
                System.err.println("| >> Certifique-se de que o Servidor RMI está em execução.");
                e.printStackTrace();
            }
            
    }

    public static void simulacaoTestePadraoSIRouSIS(ISimulacaoRemota stub, int sirOuSis){
            
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

        System.out.println("\n| >> INICIANDO SIMULAÇÃO");
        long cronometroInicio = System.currentTimeMillis();

        // Variaveis que serão incrementadas a cada ciclo. Começa com o valor do incremento.
        double taxaContagio = diferencaTaxaContagio;
        double taxaRecuperacao = diferencaTaxaRecuperacao;
        
        int contador = 0;

        if(sirOuSis == 2){
        
            try{
                // Adiciona o resultado da simulação na lista
                List<ResultadoSIR> resultadosSIR = stub.simularMultiplosModelosSIR(popTotal, infectadosInicio, taxaContagioLimite, 
                        taxaRecuperacaoLimite, tempoInicial, tempoFinal, diferencaTaxaContagio, diferencaTaxaRecuperacao);
                    
                for(ResultadoSIR resultadoSIR : resultadosSIR){
                    
                    ConsoleUtil.printResutadosSIR(contador, resultadoSIR, tempoFinal);
                    
                    // Contadores parecidos com os que estão na classe que realiza simulações
                    // Apenas para informar corretamente as taxas no terminal.
                    // Lógica de contador para incrementar todas combinações de taxas possíveis
                    if ((taxaContagio + diferencaTaxaContagio) <= taxaContagioLimite) {
                        taxaContagio += diferencaTaxaContagio;
                    } 
                    else if ((taxaRecuperacao + diferencaTaxaRecuperacao) <= taxaRecuperacaoLimite) {
                        taxaContagio = diferencaTaxaContagio; 
                        taxaRecuperacao += diferencaTaxaRecuperacao;
                    }
                                        
                    contador++;
                }
                // finaliza a cronometragem logo após a simulação e guarda o tempo de execucao
                long cronometroFinal = System.currentTimeMillis();
                long tempoExecucao = cronometroFinal - cronometroInicio; 
                System.out.println("|--------------------------------------------------------|");
                System.out.printf("| >> TEMPO DE EXECUÇÃO TOTAL: %d ms. %n", tempoExecucao);

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
                            ConsoleUtil.printResutadosSIR(index, resultadosSIR.get(index), tempoFinal);

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
                   
            try{
                List<ResultadoSIS> resultadosSIS = stub.simularMultiplosModelosSIS(popTotal, infectadosInicio, taxaContagioLimite, 
                    taxaRecuperacaoLimite, tempoInicial, tempoFinal, diferencaTaxaContagio, diferencaTaxaRecuperacao);

                contador = 0;
                for(ResultadoSIS resultadoSIS : resultadosSIS){
                    
                    ConsoleUtil.printResutadosSIS(contador, resultadoSIS, tempoFinal);
                    
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
                    Scanner scanner = new Scanner(System.in);

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


                        try{
                            System.out.println("\n|--------------------------------------------------------|");
                            System.out.printf("| DETALHES DA SIMULACAO %d \n", index);
                            ConsoleUtil.printResutadosSIS(index, resultadosSIS.get(index), tempoFinal);

                            new GraficoUtil().plotarGraficoSIS(resultadosSIS.get(index));
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
        
        try {
            // 1. Localiza o Registry no host e porta do servidor
            // Neste exemplo, assumimos que o servidor está rodando no localhost (127.0.0.1) e na porta 1099
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1099);
            
            // 2. Procura o objeto remoto pelo nome
            ISimulacaoRemota stub = (ISimulacaoRemota) registry.lookup("SimulacaoRemota");
            
            System.out.println("| >> Objeto remoto encontrado. Executando simulação SIR remota...");
            
            // Scanner para ler as entradas do sistema (system.in)
            Scanner scanner = new Scanner(System.in);
            int escolha = 1;

            while(escolha != 0){
                System.out.println("|========================================================|");
                System.out.println("|      CLIENTE: Simulação SIR e SIS - DISTRIBUIDA        |");
                System.out.println("|========================================================|");
                System.out.println("| [1] Modelo SIR e SIS - Teste simples Padrao            |");
                System.out.println("| [2] Modelo SIR - TESTE de 10 mil simulações padrões    |");
                System.out.println("| [3] Modelo SIS - TESTE de 10 mil simulações padrões    |");
                System.out.println("| [0] VOLTAR ao Menu Principal                           |");
                System.out.println("|========================================================|");
                System.out.print("| Escolha: ");
                // Recebe a escolha do usuário
                escolha = scanner.nextInt();
                System.out.println("|--------------------------------------------------------|");
                switch (escolha) {
                    case 1:
                        simulacaoTesteSimplesSIReSIS(stub);
                        break;
                    case 2:
                        simulacaoTestePadraoSIRouSIS(stub, 2);
                        break;
                    case 3:
                        simulacaoTestePadraoSIRouSIS(stub, 3);
                        break;
                    case 0:
                        System.out.println("| >> Voltando ao Menu Principal...                       |");
                        System.out.println("|--------------------------------------------------------|");
                        break;
                    default:
                        System.out.println("| Opção Inválida!");
                }
            }
        } catch (Exception e) {
            System.err.println("| >> Erro no Cliente RMI: " + e.toString());
            System.err.println("| >> Certifique-se de que o Servidor RMI está em execução.");
            e.printStackTrace();
        }
            
    }
}
