package com.grupo5.util;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

import com.grupo5.domain.resultados.ResultadoSIR;
import com.grupo5.domain.resultados.ResultadoSIS;

public class GraficoUtil {
    
    public GraficoUtil(){};
    
    public void plotarGraficoSIR(ResultadoSIR resultadoSir){
        XYChart chart = new XYChartBuilder()
                            .width(800)
                            .height(600)
                            .title("Modelo SIR Sequencial")
                            .xAxisTitle("Tempo (Dias)")
                            .yAxisTitle("População")
        .build();
                        
        // adiciona todo o histórico de dados no gráfico
        chart.addSeries("Suscetíveis (S)", resultadoSir.getTempos(), resultadoSir.getSucetiveisHistorico());
        chart.addSeries("Infectados (I)", resultadoSir.getTempos(), resultadoSir.getInfectadosHistorico());
        chart.addSeries("Recuperados (R)", resultadoSir.getTempos(), resultadoSir.getRecuperadosHistorico());

        SwingWrapper<XYChart> wrapper = new SwingWrapper<>(chart);

        // 1. Exibe o gráfico, capturando o JFrame
        javax.swing.JFrame chartFrame = wrapper.displayChart(); 

        // 2. Configure o comportamento de fechamento no JFrame capturado.
        chartFrame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    }

    public void plotarGraficoSIS(ResultadoSIS resultadoSis){
        XYChart chart = new XYChartBuilder()
                            .width(800)
                            .height(600)
                            .title("Modelo SIS Sequencial")
                            .xAxisTitle("Tempo (Dias)")
                            .yAxisTitle("População")
        .build();
                        
        // adiciona todo o histórico de dados no gráfico
        chart.addSeries("Suscetíveis (S)", resultadoSis.getTempos(), resultadoSis.getSucetiveisHistorico());
        chart.addSeries("Infectados (I)", resultadoSis.getTempos(), resultadoSis.getInfectadosHistorico());

        /* 
        SwingWrapper wrapper = new SwingWrapper<>(chart);
        
        wrapper.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        wrapper.displayChart();
        */
        
        SwingWrapper<XYChart> wrapper = new SwingWrapper<>(chart);

        // 1. Exibe o gráfico, capturando o JFrame
        javax.swing.JFrame chartFrame = wrapper.displayChart(); 

        // 2. Configure o comportamento de fechamento no JFrame capturado.
        chartFrame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    }
}
