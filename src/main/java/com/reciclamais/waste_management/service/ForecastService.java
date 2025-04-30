package com.reciclamais.waste_management.service;

import com.reciclamais.waste_management.dto.WastePredictionDTO;
import com.reciclamais.waste_management.model.Waste;
import com.reciclamais.waste_management.repository.WasteRepository;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Serviço responsável pela previsão de resíduos e análise de tendências.
 * 
 * Este serviço utiliza técnicas estatísticas para:
 * - Prever a quantidade de resíduos para o próximo mês
 * - Calcular o nível de confiança das previsões
 * - Analisar tendências históricas
 * 
 * Principais funcionalidades:
 * 1. Previsão de Resíduos
 *    - Análise de dados históricos
 *    - Regressão linear para previsão
 *    - Cálculo de confiança estatística
 * 
 * 2. Análise de Dados
 *    - Agrupamento por período
 *    - Cálculo de médias e tendências
 *    - Validação de dados suficientes
 * 
 * 3. Tratamento de Dados
 *    - Normalização de valores
 *    - Tratamento de casos especiais
 *    - Garantia de valores válidos
 * 
 * @author Sistema de Gestão de Resíduos
 * @version 1.0
 */
@Service
public class ForecastService {

    private static final Logger logger = LoggerFactory.getLogger(ForecastService.class);
    
    private final WasteRepository wasteRepository;

    public ForecastService(WasteRepository wasteRepository) {
        this.wasteRepository = wasteRepository;
        logger.info("ForecastService construído com sucesso");
    }

    /**
     * Prevé a quantidade de resíduos para o próximo mês.
     * 
     * Este método implementa as seguintes etapas:
     * 1. Coleta dados históricos de resíduos
     * 2. Agrupa resíduos por mês
     * 3. Aplica regressão linear para previsão
     * 4. Calcula o coeficiente de determinação (R²)
     * 5. Retorna a previsão com nível de confiança
     * 
     * Requisitos para previsão:
     * - Mínimo de 2 meses de dados históricos
     * - Dados suficientes para regressão
     * 
     * @return DTO contendo a previsão e nível de confiança
     *         - predictedAmount: Quantidade prevista em kg
     *         - confidence: Nível de confiança entre 0 e 1
     */
    public WastePredictionDTO predictNextMonthWaste() {
        List<Waste> allWastes = wasteRepository.findAll();
        logger.info("Calculating waste prediction from {} wastes", allWastes.size());

        if (allWastes.isEmpty() || allWastes.size() < 2) {
            return new WastePredictionDTO(0.0, 0.0);
        }

        // Agrupa resíduos por mês
        Map<LocalDate, Double> monthlyWaste = allWastes.stream()
                .collect(Collectors.groupingBy(
                        waste -> waste.getDate().withDayOfMonth(1),
                        Collectors.summingDouble(Waste::getWeight)
                ));

        if (monthlyWaste.size() < 2) {
            return new WastePredictionDTO(0.0, 0.0);
        }

        // Prepara dados para regressão linear
        List<LocalDate> sortedDates = new ArrayList<>(monthlyWaste.keySet());
        Collections.sort(sortedDates);

        double[] x = new double[sortedDates.size()];
        double[] y = new double[sortedDates.size()];

        for (int i = 0; i < sortedDates.size(); i++) {
            x[i] = i;
            y[i] = monthlyWaste.get(sortedDates.get(i));
        }

        // Cria e ajusta o modelo de regressão linear
        SimpleRegression regression = new SimpleRegression();
        for (int i = 0; i < x.length; i++) {
            regression.addData(x[i], y[i]);
        }

        // Faz a previsão para o próximo mês
        double nextMonthPrediction = regression.predict(x.length);
        
        // Calcula o coeficiente de determinação (R²) como medida de confiança
        double confidence = regression.getRSquare();

        // Garante que os valores sejam válidos
        if (Double.isNaN(nextMonthPrediction) || nextMonthPrediction < 0) {
            nextMonthPrediction = 0.0;
        }
        if (Double.isNaN(confidence) || confidence < 0) {
            confidence = 0.0;
        }

        logger.info("Waste prediction for next month: {} kg (confidence: {})", 
                   nextMonthPrediction, confidence);

        return new WastePredictionDTO(nextMonthPrediction, confidence);
    }
}
