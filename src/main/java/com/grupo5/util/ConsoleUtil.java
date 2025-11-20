package com.grupo5.util;

import java.io.IOException;

import com.grupo5.domain.resultados.ResultadoSIR;
import com.grupo5.domain.resultados.ResultadoSIS;

/**
 * @author pauloandre7
 * 
 * Essa classe é apenas um utilitário para manipular o terminal.
 */

public class ConsoleUtil {
    
    // Limpa o terminal com base no Sistema Operacional que está sendo usado
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

    public static void printResutadosSIR(int index, ResultadoSIR resultadoSIR, double tempoFinal){
        // expõe os resultados no terminal usando o objeto ResultadoSIR.
        System.out.println("|--------------------------------------------------------|");
        System.out.printf("| >> PARÂMETROS DA SIMULAÇÃO %d: %n", index);
        System.out.printf("|    - População: %.2f \n", resultadoSIR.getPopTotal());
        System.out.printf("|    - Taxa de Contagio: %.2f \n", resultadoSIR.getTaxaContagio());
        System.out.printf("|    - Taxa de Recuperação: %.2f \n", resultadoSIR.getTaxaRecuperacao());
        System.out.printf("| >> RESULTADOS DA SIMULAÇÃO %d: %n", index);
        System.out.printf("|    - DIA %.1f -> S: %.2f, I: %.2f, R: %.2f %n", tempoFinal, 
                                                                        resultadoSIR.getSucetiveisHistorico().getLast(),
                                                                        resultadoSIR.getInfectadosHistorico().getLast(), 
                                                                        resultadoSIR.getRecuperadosHistorico().getLast()
        );
    }

    public static void printResutadosSIS(int index, ResultadoSIS resultadoSIS, double tempoFinal){
        // expõe os resultados no terminal usando o objeto ResultadoSIR.
        System.out.println("|--------------------------------------------------------|");
        System.out.printf("| >> PARÂMETROS DA SIMULAÇÃO %d: %n", index);
        System.out.printf("|    - População: %.2f \n", resultadoSIS.getPopTotal());
        System.out.printf("|    - Taxa de Contagio: %.2f \n", resultadoSIS.getTaxaContagio());
        System.out.printf("|    - Taxa de Recuperação: %.2f \n", resultadoSIS.getTaxaRecuperacao());
        System.out.printf("| >> RESULTADOS DA SIMULAÇÃO %d: %n", index);
        System.out.printf("|    - DIA %.1f -> S: %.2f, I: %.2f %n", tempoFinal, 
                                                                resultadoSIS.getSucetiveisHistorico().getLast(),
                                                                resultadoSIS.getInfectadosHistorico().getLast()
        );
}
}
