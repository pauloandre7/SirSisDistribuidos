package com.grupo5.application.coletores;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;

public class SISColetorDeResultados implements StepHandler{

    // Listas para armazenar os resultados
    private List<Double> tempos = new ArrayList<>();
    private List<double[]> estados = new ArrayList<>();

    private List<Double> sHistorico = new ArrayList<>();
    private List<Double> iHistorico = new ArrayList<>();

    @Override
    public void init(double t0, double[] y0, double tFinal) {
        // t0: Tempo inicial (geralmente 0)
        // y0: Estado inicial [S0, I0, R0]
        // tFinal: Tempo alvo da integração

        // Limpa as listas, caso o solver seja reutilizado
        tempos.clear();
        estados.clear();
        sHistorico.clear();
        iHistorico.clear();

        // O estado inicial é a primeiro posicao.
        tempos.add(t0);
        // Usa .clone() para copiar o array pois o solver pode reutilizar o array y0.
        estados.add(y0.clone()); 

        sHistorico.add(y0[0]);
        iHistorico.add(y0[1]);
    }

    @Override
    public void handleStep(StepInterpolator interpolator, boolean isLast) throws MaxCountExceededException {
        // interpolator: Objeto que permite obter dados do passo
        // isLast: true se este for o último passo da integração

        // Pega o tempo no final deste passo
        double t = interpolator.getCurrentTime();

        // Obtém o estado no tempo 't' (o vetor [S, I, R])
        // O método getInterpolatedState() armazena o resultado em um array
        double[] estadoAtual = interpolator.getInterpolatedState();

        // Armazena os resultados
        tempos.add(t);

        // tem que usar clone por causa do Interpolator, que sempre usa o mesmo vetor.
        estados.add(estadoAtual.clone()); 
        
        sHistorico.add(estadoAtual[0]);
        iHistorico.add(estadoAtual[1]);

        // Só para acompanhar:
        System.out.printf("| Tempo: %.2f, Infectados: %.2f%n", t, estadoAtual[1]);
    }
    
    // Getters (retornam cópias)
    public List<Double> getTempos() {
        return new ArrayList<>(tempos);
    }

    public List<double[]> getEstados() {
        List<double[]> copy = new ArrayList<>(estados.size());
        for (double[] e : estados) {
            copy.add(e.clone());
        }
        return copy;
    }

    public List<Double> getSHistorico() {
        return new ArrayList<>(sHistorico);
    }

    public List<Double> getIHistorico() {
        return new ArrayList<>(iHistorico);
    }

}
