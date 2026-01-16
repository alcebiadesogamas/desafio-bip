package com.example.ejb.service.exception;

public class NoBalanceException extends BusinessException {
    private static final String MESSAGE = "Saldo insuficiente!";

    public NoBalanceException() {
        super(MESSAGE);
    }
}
