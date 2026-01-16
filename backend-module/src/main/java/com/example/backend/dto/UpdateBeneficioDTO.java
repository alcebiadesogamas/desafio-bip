package com.example.backend.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record UpdateBeneficioDTO(
        @NotNull
        Long id,
        String name,
        String description,
        BigDecimal value,
        Boolean active
) {
}