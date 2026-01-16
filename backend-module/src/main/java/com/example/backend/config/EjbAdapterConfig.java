package com.example.backend.config;

import com.example.ejb.service.BeneficioEjbService;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EjbAdapterConfig {

    @Bean
    public BeneficioEjbService beneficioEjbService(EntityManager em) {
        BeneficioEjbService ejb = new BeneficioEjbService();
        ejb.setEntityManager(em);
        return ejb;
    }
}
