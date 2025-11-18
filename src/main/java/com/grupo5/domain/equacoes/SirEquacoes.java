package com.grupo5.domain.equacoes;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;

/**
 * @author pauloandre7
 * 
 * Essa classe é para implementar a interface do solver de derivadas da apache.
 * Aqui estão alocadas as equações do modelo SIR que o solver terá que resolver.
 * 
 * * EQUAÇÕES do Sistema:
 * dS/dt -->  -taxaContagio * S * I / popTotal
 * dI/dt -->  (taxaContagio * S * I / popTotal) - (taxaRecuperacao * I)
 * dR/dt -->  taxaRecuperacao * I
 */
public class SirEquacoes implements FirstOrderDifferentialEquations {
    
    private double taxaContagio;
    private double taxaRecuperacao;
    private double popTotal;

    
    public SirEquacoes(double taxaContagio, double taxaRecuperacao, double popTotal) {
        this.taxaContagio = taxaContagio;
        this.taxaRecuperacao = taxaRecuperacao;
        this.popTotal = popTotal;
    }

    // O solver pede as fórmulas por esse método aqui.
    @Override
    public void computeDerivatives(double t, double[] estadoAtual, double[] derivadas) {
        
        double S = estadoAtual[0];
        double I = estadoAtual[1];
        // double R = estadoAtual[2]; // Não precisa dessa variavel para os cálculos de dS e dI

        // Sem return dos resultados, apenas preenche o array do parâmetro
        
        // Equação 1: dS/dt
        derivadas[0] = -this.taxaContagio * S * I / this.popTotal;
        
        // Equação 2: dI/dt
        derivadas[1] = (this.taxaContagio * S * I / this.popTotal) - (this.taxaRecuperacao * I);
        
        // Equação 3: dR/dt
        derivadas[2] = this.taxaRecuperacao * I;
    }

    /*
     * Método exigido pela interface.
     * (apenas informa ao solver a quantidade de equações).
     */
    @Override
    public int getDimension() {
        return 3;
    }
}
