package com.example.backend.service;

import com.example.backend.dto.BeneficioDTO;
import com.example.backend.dto.UpdateBeneficioDTO;
import com.example.backend.service.mapper.BeneficioMapper;
import com.example.backend.repository.BeneficioRepository;
import com.example.ejb.model.Beneficio;
import com.example.ejb.service.exception.BeneficioNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BeneficioServiceImplUnitTest {

    @Mock
    private BeneficioRepository repository;

    private BeneficioServiceImpl service;

    private final BeneficioMapper mapper = BeneficioMapper.getInstance();

    private final AtomicLong idSeq = new AtomicLong(1);

    @BeforeEach
    void setUp() {
        service = new BeneficioServiceImpl(repository);

        lenient().when(repository.save(any(com.example.ejb.model.Beneficio.class))).thenAnswer(invocation -> {
            Object entity = invocation.getArgument(0);
            setEntityId(entity, idSeq.getAndIncrement());
            return entity;
        });
    }

    @Test
    @DisplayName("getAll deve retornar DTOs mapeados")
    void getAllShouldReturnDtos() {
        var dto1 = new BeneficioDTO(null, "A", "d1", BigDecimal.valueOf(10), true);
        var dto2 = new BeneficioDTO(null, "B", "d2", BigDecimal.valueOf(20), true);
        var e1 = mapper.toBeneficio(dto1);
        var e2 = mapper.toBeneficio(dto2);
        setEntityId(e1, 100L);
        setEntityId(e2, 101L);

        when(repository.findAll()).thenReturn(List.of(e1, e2));

        var result = service.getAll();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(BeneficioDTO::name).containsExactlyInAnyOrder("A", "B");
    }

    @Test
    @DisplayName("getOne retorna DTO quando encontrado")
    void getOneShouldReturnDtoWhenFound() {
        var dto = new BeneficioDTO(null, "One", "d", BigDecimal.valueOf(5), true);
        var entity = mapper.toBeneficio(dto);
        setEntityId(entity, 200L);

        when(repository.findById(200L)).thenReturn(Optional.of(entity));

        var result = service.getOne(200L);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(200L);
        assertThat(result.name()).isEqualTo("One");
    }

    @Test
    @DisplayName("getOne lança exceção quando não encontrado")
    void getOneShouldThrowWhenNotFound() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(BeneficioNotFoundException.class, () -> service.getOne(999L));
    }

    @Test
    @DisplayName("create salva e retorna DTO com id atribuído")
    void createShouldSaveAndReturnDto() {
        var dto = new BeneficioDTO(null, "Create", "d", BigDecimal.valueOf(15), true);

        var result = service.create(dto);

        assertThat(result).isNotNull();
        assertThat(result.id()).isNotNull();
        assertThat(result.name()).isEqualTo("Create");

        var captor = ArgumentCaptor.forClass(Beneficio.class);
        verify(repository).save(captor.capture());
        Beneficio savedEntity = captor.getValue();
        var savedDto = mapper.toBeneficioDTO(savedEntity);
        assertThat(savedDto.name()).isEqualTo("Create");
    }

    @Test
    @DisplayName("update modifica existente e retorna DTO atualizado")
    void updateShouldModifyAndReturnUpdated() {
        var existingDto = new BeneficioDTO(null, "Old", "oldDesc", BigDecimal.valueOf(30), true);
        var existingEntity = mapper.toBeneficio(existingDto);
        setEntityId(existingEntity, 300L);

        when(repository.findById(300L)).thenReturn(Optional.of(existingEntity));
        when(repository.save(any(com.example.ejb.model.Beneficio.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var updateDto = new UpdateBeneficioDTO(300L, "New", "newDesc", BigDecimal.valueOf(40), false);

        var updated = service.update(updateDto);

        assertThat(updated.id()).isEqualTo(300L);
        assertThat(updated.name()).isEqualTo("New");
        assertThat(updated.description()).isEqualTo("newDesc");
        assertThat(updated.value()).isEqualByComparingTo(BigDecimal.valueOf(40));
        assertThat(updated.active()).isFalse();
    }

    @Test
    @DisplayName("partialUpdate atualiza apenas os campos fornecidos")
    void partialUpdateShouldChangeProvidedFieldsOnly() {
        var existingDto = new BeneficioDTO(null, "P_old", "pdesc", BigDecimal.valueOf(60), true);
        var existingEntity = mapper.toBeneficio(existingDto);
        setEntityId(existingEntity, 400L);

        when(repository.findById(400L)).thenReturn(Optional.of(existingEntity));
        when(repository.save(any(com.example.ejb.model.Beneficio.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var partial = new UpdateBeneficioDTO(400L, "P_new", null, null, null);

        var result = service.partialUpdate(partial);

        assertThat(result.id()).isEqualTo(400L);
        assertThat(result.name()).isEqualTo("P_new");
        assertThat(result.description()).isEqualTo("pdesc");
        assertThat(result.value()).isEqualByComparingTo(BigDecimal.valueOf(60));
    }

    @Test
    @DisplayName("delete remove quando existe")
    void deleteShouldRemoveWhenExists() {
        var existingDto = new BeneficioDTO(null, "D", "desc", BigDecimal.valueOf(7), true);
        var existingEntity = mapper.toBeneficio(existingDto);
        setEntityId(existingEntity, 500L);

        when(repository.existsById(500L)).thenReturn(true);
        when(repository.findById(500L)).thenReturn(Optional.of(existingEntity));

        service.delete(500L);

        verify(repository).delete(existingEntity);
    }

    @Test
    @DisplayName("delete lança exceção quando não encontrado")
    void deleteShouldThrowWhenNotFound() {
        when(repository.existsById(888L)).thenReturn(false);

        assertThrows(BeneficioNotFoundException.class, () -> service.delete(888L));
    }

    private static void setEntityId(Object entity, Long id) {
        if (entity == null) return;
        try {
            Method m = entity.getClass().getMethod("setId", Long.class);
            m.invoke(entity, id);
            return;
        } catch (Exception ignored) { /* ignora e tenta alternativa */ }

        try {
            Method m2 = entity.getClass().getMethod("setId", long.class);
            m2.invoke(entity, id);
            return;
        } catch (Exception ignored) { /* ignora e tenta alternativa */ }

        try {
            Field f = entity.getClass().getDeclaredField("id");
            f.setAccessible(true);
            f.set(entity, id);
        } catch (Exception ignored) { /* ignora */ }
    }
}
