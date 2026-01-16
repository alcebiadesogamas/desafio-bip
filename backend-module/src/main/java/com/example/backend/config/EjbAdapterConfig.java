package com.example.backend.config;

import com.example.ejb.service.BeneficioEjbService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EjbAdapterConfig {

    @Bean
    public BeneficioEjbService beneficioEjbService() {
        return new BeneficioEjbService();
    }
}
