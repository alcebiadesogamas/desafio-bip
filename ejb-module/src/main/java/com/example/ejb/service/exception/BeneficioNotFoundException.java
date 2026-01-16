package com.example.ejb.service.exception;

public class BeneficioNotFoundException extends BusinessException {

    public BeneficioNotFoundException(Long id) {
        super("Benefício com id = " + id + " não encontrado");
    }
}