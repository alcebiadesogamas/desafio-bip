package com.example.ejb.service.exception;

public class InvalidAmountException extends BusinessException {

    private static final String MESSAGE = "Valor invalido!";

    public InvalidAmountException() {
        super(MESSAGE);
    }
}
