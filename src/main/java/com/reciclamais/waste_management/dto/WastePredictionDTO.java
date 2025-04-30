package com.reciclamais.waste_management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WastePredictionDTO {
    private double predictedAmount;
    private double confidence;
} 