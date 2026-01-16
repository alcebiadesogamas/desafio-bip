package com.example.backend.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransferDTO (@NotNull Long fromId,
                           @NotNull Long toId,
                           @NotNull BigDecimal amount) {}
