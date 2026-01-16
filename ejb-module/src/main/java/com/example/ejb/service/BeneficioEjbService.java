package com.example.ejb.service;

import com.example.ejb.model.Beneficio;
import com.example.ejb.service.exception.BeneficioNotFoundException;
import com.example.ejb.service.exception.NoBalanceException;
import com.example.ejb.service.exception.SameBeneficioException;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.logging.Logger;

import com.example.ejb.service.exception.InvalidAmountException;
import jakarta.transaction.Transactional;

@Stateless
public class BeneficioEjbService {

    @PersistenceContext
    private EntityManager em;

    private static final Logger log =
            Logger.getLogger(BeneficioEjbService.class.getName());

    @Transactional(rollbackOn = Exception.class)
    public void transfer(Long fromId, Long toId, BigDecimal amount) {

        log.info(() -> "Transferindo valor de " + fromId + " para " + toId);

        validateTransfer(fromId, toId, amount);

        Beneficio from = findBeneficio(fromId);
        Beneficio to   = findBeneficio(toId);

        validateBalance(from, amount);

        applyTransfer(from, to, amount);
    }

    private void validateTransfer(Long fromId, Long toId, BigDecimal amount) {

        if (fromId.equals(toId)) {
            throw new SameBeneficioException();
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException();
        }
    }

    private Beneficio findBeneficio(Long id) {
        Beneficio beneficio =
                em.find(Beneficio.class, id, LockModeType.OPTIMISTIC);

        if (beneficio == null) {
            throw new BeneficioNotFoundException(id);
        }

        return beneficio;
    }

    private void validateBalance(Beneficio from, BigDecimal amount) {
        if (from.getValue().compareTo(amount) < 0) {
            throw new NoBalanceException();
        }
    }

    private void applyTransfer(Beneficio from, Beneficio to, BigDecimal amount) {
        from.setValue(from.getValue().subtract(amount));
        to.setValue(to.getValue().add(amount));
    }

    // apenas para o teste
    protected void setEntityManager(EntityManager em) {
        this.em = em;
    }
}

