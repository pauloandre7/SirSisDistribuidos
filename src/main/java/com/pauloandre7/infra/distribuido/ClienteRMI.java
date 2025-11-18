package com.pauloandre7.infra.distribuido;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

/**
 * Classe principal para o Cliente RMI.
 * 
 * Fonte: Adaptado do exemplo de RMI fornecido pelo professor (AulaRMI(1).zip)
 */
public class ClienteRMI {
    
    public static void main(String[] args) {
        try {
            // 1. Localiza o Registry no host e porta do servidor
            // Neste exemplo, assumimos que o servidor está rodando no localhost (127.0.0.1) e na porta 1099
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1099);
            
            // 2. Procura o objeto remoto pelo nome
            ISimulacaoRemota stub = (ISimulacaoRemota) registry.lookup("SimulacaoRemota");
            
            System.out.println("| >> Objeto remoto encontrado. Executando simulação SIR remota...");
            
            // 3. Chama o método remoto (Simulação SIR com dados de exemplo)
            double popTotal = 1000.0;
            double infectadosInicio = 10.0;
            double taxaContagio = 0.3;
            double taxaRecuperacao = 0.1;
            double tempoInicial = 0.0;
            double tempoFinal = 100.0;
            
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
            
        } catch (Exception e) {
            System.err.println("| >> Erro no Cliente RMI: " + e.toString());
            System.err.println("| >> Certifique-se de que o Servidor RMI está em execução.");
            e.printStackTrace();
        }
    }
}
