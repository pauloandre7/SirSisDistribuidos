package com.pauloandre7.equacoes;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;

/**
 * @author pauloandre7
 * 
 * Essa classe implementa a interface do solver de derivadas da apache.
 * Aqui estão alocadas as equações do Modelo SIS que o solver terá que resolver
 * 
 * O modelo SIS é usado para doenças que não fornecem cura após a primeira infecção,
 * então ele não possui a variável Recuperados.
 * 
 * * EQUAÇÕES do Sistema:
 * dS/dt -->  (taxaRecuperacao * I) - (taxaContagio * S * I / popTotal);
 * dI/dt -->  (taxaContagio * S * I / popTotal) - (taxaRecuperacao * I);
 */

public class SisEquacoes implements FirstOrderDifferentialEquations{

    private double taxaContagio;
    private double taxaRecuperacao;
    private double popTotal;

    public SisEquacoes(double taxaContagio, double taxaRecuperacao, double popTotal){
        this.taxaContagio = taxaContagio;
        this.taxaRecuperacao = taxaRecuperacao;
        this.popTotal = popTotal;
    }

    @Override
    public int getDimension() {
        return 2;
    }

    // O solver pede as fórmulas por esse método aqui.
    @Override
    public void computeDerivatives(double t, double[] estadoAtual, double[] derivadas) {
        
        double S = estadoAtual[0];
        double I = estadoAtual[1];
        // double R = estadoAtual[2]; // Não precisa dessa variavel para os cálculos de dS e dI

        // Sem return dos resultados, apenas preenche o array do parâmetro
        
        // Equação 1: dS/dt
        derivadas[0] = (taxaRecuperacao * I) - (taxaContagio * S * I / popTotal);
        
        // Equação 2: dI/dt
        derivadas[1] = (taxaContagio * S * I / popTotal) - (taxaRecuperacao * I);
        
    }
    
}
