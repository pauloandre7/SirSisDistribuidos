package com.grupo5.domain.resultados;

import java.util.List;

/**
 * @author pauloandre7
 * 
 * A classe não acrescenta nada novo, mas fiz mesmo assim pelo bem do Poliformismo.
 */
public class ResultadoSIS extends Resultado {

    public ResultadoSIS(List<Double> tempos, List<Double> sucetiveisHistorico, List<Double> infectadosHistorico) {
        super(tempos, sucetiveisHistorico, infectadosHistorico);
    }
    
}
