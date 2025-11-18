package com.pauloandre7.distribuido;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Classe principal para iniciar o Servidor RMI.
 * 
 * Fonte: Adaptado do exemplo de RMI fornecido pelo professor (AulaRMI(1).zip)
 */
public class ServidorRMI {
    
    public static void main(String[] args) {
        try {
            // 1. Cria a implementação do objeto remoto
            SimulacaoRemotaImpl objRemoto = new SimulacaoRemotaImpl();
            
            // 2. Cria o Registry na porta padrão (1099) ou em outra porta
            Registry registry = LocateRegistry.createRegistry(1099);
            
            // 3. Registra o objeto remoto no Registry com um nome
            registry.rebind("SimulacaoRemota", objRemoto);
            
            System.out.println("Servidor RMI de Simulação SIR/SIS pronto na porta 1099.");
            System.out.println("Aguardando chamadas de clientes...");
            
        } catch (Exception e) {
            System.err.println("Erro no Servidor RMI: " + e.toString());
            e.printStackTrace();
        }
    }
}
