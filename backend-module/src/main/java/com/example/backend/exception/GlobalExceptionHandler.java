package com.example.backend.exception;

import com.example.ejb.service.exception.*;
import jakarta.persistence.OptimisticLockException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            InvalidAmountException.class,
            NoBalanceException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBusiness(BusinessException ex) {
        return new ApiError("BUSINESS_ERROR", ex.getMessage());
    }

    @ExceptionHandler(BeneficioNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFound(BeneficioNotFoundException ex) {
        return new ApiError("NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(OptimisticLockException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleOptimisticLock() {
        return new ApiError(
                "CONCURRENCY_CONFLICT",
                "O registro foi alterado por outra transação. Tente novamente."
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleGeneric(Exception ex) {
        return new ApiError("INTERNAL_ERROR", "Erro inesperado");
    }
}