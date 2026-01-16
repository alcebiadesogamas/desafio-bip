package com.example.backend.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.math.BigDecimal;

public record BeneficioDTO(
        Long id,
        @NotNull
        String name,
        String description,
        @NotNull
        BigDecimal value,
        @DefaultValue("true")
        Boolean active
) {
}
