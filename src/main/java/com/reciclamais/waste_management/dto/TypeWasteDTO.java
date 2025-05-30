package com.reciclamais.waste_management.dto;

import lombok.*;

/**
 * Data Transfer Object (DTO) para representar os tipos de resíduos.
 * Este DTO é utilizado para transferir dados entre a camada de serviço e a camada de apresentação.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class TypeWasteDTO {
    private String type;
    private int quantity;
    private double percentage;
}
