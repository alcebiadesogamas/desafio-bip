package com.example.ejb.service;

import jakarta.ejb.Remote;
import java.math.BigDecimal;

@Remote
public interface BeneficioEjbServiceRemote {

    void transfer(Long fromId, Long toId, BigDecimal amount);
}