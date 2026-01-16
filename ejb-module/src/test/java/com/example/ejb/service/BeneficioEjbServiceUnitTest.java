package com.example.ejb.service;

import com.example.ejb.model.Beneficio;
import com.example.ejb.service.exception.BeneficioNotFoundException;
import com.example.ejb.service.exception.InvalidAmountException;
import com.example.ejb.service.exception.NoBalanceException;
import com.example.ejb.service.exception.SameBeneficioException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BeneficioEjbServiceUnitTest {

    @Mock
    private EntityManager em;

    private BeneficioEjbService service;

    @BeforeEach
    void setUp() {
        service = new BeneficioEjbService();
        service.setEntityManager(em);
    }

    private Beneficio beneficioOf(Long id, String name, BigDecimal value) {
        Beneficio b = new Beneficio();
        b.setId(id);
        b.setName(name);
        b.setValue(value);
        b.setDescription("d");
        b.setActive(true);
        b.setVersion(1L);
        return b;
    }

    @Test
    @DisplayName("transfer deve mover saldo quando ambos benefícios existem e há saldo suficiente")
    void transferShouldMoveBalanceWhenSufficient() {
        Beneficio from = beneficioOf(1L, "from", BigDecimal.valueOf(100));
        Beneficio to = beneficioOf(2L, "to", BigDecimal.valueOf(50));

        when(em.find(eq(Beneficio.class), eq(1L), any(LockModeType.class))).thenReturn(from);
        when(em.find(eq(Beneficio.class), eq(2L), any(LockModeType.class))).thenReturn(to);

        service.transfer(1L, 2L, BigDecimal.valueOf(10));

        assertEquals(BigDecimal.valueOf(90), from.getValue());
        assertEquals(BigDecimal.valueOf(60), to.getValue());
    }

    @Test
    @DisplayName("transfer deve lançar BeneficioNotFoundException quando origem não encontrada")
    void transferShouldThrowWhenFromNotFound() {
        when(em.find(eq(Beneficio.class), eq(1L), any(LockModeType.class))).thenReturn(null);

        assertThrows(BeneficioNotFoundException.class, () -> service.transfer(1L, 2L, BigDecimal.valueOf(10)));
    }

    @Test
    @DisplayName("transfer deve lançar BeneficioNotFoundException quando destino não encontrado")
    void transferShouldThrowWhenToNotFound() {
        Beneficio from = beneficioOf(1L, "from", BigDecimal.valueOf(100));
        when(em.find(eq(Beneficio.class), eq(1L), any(LockModeType.class))).thenReturn(from);
        when(em.find(eq(Beneficio.class), eq(2L), any(LockModeType.class))).thenReturn(null);

        assertThrows(BeneficioNotFoundException.class, () -> service.transfer(1L, 2L, BigDecimal.valueOf(10)));
    }

    @Test
    @DisplayName("transfer deve lançar NoBalanceException quando saldo insuficiente")
    void transferShouldThrowWhenNoBalance() {
        Beneficio from = beneficioOf(1L, "from", BigDecimal.valueOf(5));
        Beneficio to = beneficioOf(2L, "to", BigDecimal.valueOf(0));

        when(em.find(eq(Beneficio.class), eq(1L), any(LockModeType.class))).thenReturn(from);
        when(em.find(eq(Beneficio.class), eq(2L), any(LockModeType.class))).thenReturn(to);

        assertThrows(NoBalanceException.class, () -> service.transfer(1L, 2L, BigDecimal.valueOf(10)));
    }

    @Test
    @DisplayName("transfer deve lançar SameBeneficioException quando fromId == toId")
    void transferShouldThrowWhenSameIds() {
        assertThrows(SameBeneficioException.class, () -> service.transfer(1L, 1L, BigDecimal.valueOf(10)));
    }

    @Test
    @DisplayName("transfer deve lançar InvalidAmountException quando amount for nulo ou <= 0")
    void transferShouldThrowWhenInvalidAmount() {
        assertThrows(InvalidAmountException.class, () -> service.transfer(1L, 2L, BigDecimal.ZERO));
        assertThrows(InvalidAmountException.class, () -> service.transfer(1L, 2L, BigDecimal.valueOf(-1)));
        assertThrows(InvalidAmountException.class, () -> service.transfer(1L, 2L, null));
    }
}