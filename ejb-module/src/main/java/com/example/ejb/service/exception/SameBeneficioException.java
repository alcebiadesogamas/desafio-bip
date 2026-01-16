package com.example.ejb.service.exception;

public class SameBeneficioException extends BusinessException {
    private static final String MESSAGE = "Benefícios de origem e destino não podem ser iguais";
    public SameBeneficioException() {
        super(MESSAGE);
    }
}