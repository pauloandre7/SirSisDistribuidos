package com.grupo5.util;

import java.io.IOException;

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
}
