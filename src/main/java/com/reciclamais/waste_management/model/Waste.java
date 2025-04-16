package com.reciclamais.waste_management.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_waste")
public class Waste {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "O tipo de resíduo é obrigatório")
    @Enumerated(EnumType.STRING)
    private Type type;

    @NotNull(message = "O peso é obrigatório")
    @Positive(message = "O peso deve ser maior que zero")
    private Double weight;

    @NotNull(message = "A data é obrigatória")
    @Column(nullable = false)
    private LocalDate date;

    @NotNull(message = "A descrição é obrigatória")
    @Size(min = 10, message = "A descrição deve ter pelo menos 10 caracteres")
    @Column(length = 500)
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
