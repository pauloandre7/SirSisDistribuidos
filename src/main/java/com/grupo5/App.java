package com.grupo5;

import java.util.Scanner;

import com.grupo5.infra.distribuido.ClienteRMI;
import com.grupo5.infra.sequencial.SimulacaoSequencial;
import com.grupo5.util.ConsoleUtil;

/**
 * Classe principal do projeto SirSisDistribuidos.
 * Permite a execução das simulações nos modos Sequencial, Paralelo e Distribuído.
 */
public class App 
{
    public static void main( String[] args )
    {
        Scanner scanner = new Scanner(System.in);
        int escolha = -1;

        while(escolha != 0){
            System.out.println("|=====================================================================|");
            System.out.println("|                  SirSisDistribuidos - Menu Principal                |");
            System.out.println("|=====================================================================|");
            System.out.println("| [1] Simulação Sequencial (SIR/SIS)                                  |");
            System.out.println("| [2] Simulação Paralela (NÃO IMPLEMENTADO)                           |");
            System.out.println("| [3] Simulação Distribuída (Cliente RMI)                             |");
            System.out.println("| [4] Iniciar Servidor RMI (A ser executado em outra máquina/terminal)|");
            System.out.println("| [0] SAIR                                                            |");
            System.out.println("|=====================================================================|");
            System.out.print("| Escolha: ");
            
            if (scanner.hasNextInt()) {
                escolha = scanner.nextInt();
                scanner.nextLine(); // Consumir a nova linha
            } else {
                System.out.println("| Entrada inválida. Tente novamente.");
                scanner.nextLine(); // Consumir a entrada inválida
                continue;
            }
            
            System.out.println("|---------------------------------------------------------------------|");
            switch (escolha) {
                case 1:
                    ConsoleUtil.clearConsoleOS();
                    SimulacaoSequencial.main(new String[]{});
                    break;
                case 2:
                    System.out.println("| >> Funcionalidade Paralela ainda não implementada.     |");
                    break;
                case 3:
                    ConsoleUtil.clearConsoleOS();
                    ClienteRMI.main(new String[]{});
                    break;
                case 4:
                    System.out.println("| >> Iniciando Servidor RMI. Pressione Ctrl+C para parar.|");
                    com.grupo5.infra.distribuido.ServidorRMI.main(new String[]{});
                    break;
                case 0:
                    System.out.println("| >> Encerrando o programa.                                           |");
                    break;
                default:
                    System.out.println("| Opção Inválida!");
            }
            System.out.println("|---------------------------------------------------------------------|");
        }
        scanner.close();
    }
}
