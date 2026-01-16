package com.example.backend.service.mapper;

import com.example.backend.dto.BeneficioDTO;
import com.example.backend.dto.UpdateBeneficioDTO;
import com.example.ejb.model.Beneficio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class BeneficioMapperTest {

    private final BeneficioMapper mapper = BeneficioMapper.getInstance();

    @Test
    @DisplayName("Deve mapear BeneficioDTO para Beneficio corretamente")
    void shouldMapBeneficioDTOToEntity() {
        var dto = new BeneficioDTO(null, "Nome", "Descricao", BigDecimal.valueOf(12), true);

        Beneficio entity = mapper.toBeneficio(dto);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isNull();
        assertThat(entity.getName()).isEqualTo(dto.name());
        assertThat(entity.getDescription()).isEqualTo(dto.description());
        assertThat(entity.getValue()).isEqualByComparingTo(dto.value());
        assertThat(entity.getActive()).isEqualTo(dto.active());
    }

    @Test
    @DisplayName("Deve mapear UpdateBeneficioDTO para Beneficio corretamente")
    void shouldMapUpdateBeneficioDTOToEntity() {
        var updateDto = new UpdateBeneficioDTO(123L, "Nome2", "Desc2", BigDecimal.valueOf(5), false);

        Beneficio entity = mapper.toBeneficio(updateDto);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isNull();
        assertThat(entity.getName()).isEqualTo(updateDto.name());
        assertThat(entity.getDescription()).isEqualTo(updateDto.description());
        assertThat(entity.getValue()).isEqualByComparingTo(updateDto.value());
        assertThat(entity.getActive()).isEqualTo(updateDto.active());
    }

    @Test
    @DisplayName("Deve mapear Beneficio para BeneficioDTO e UpdateBeneficioDTO corretamente")
    void shouldMapEntityToDtos() {
        Beneficio b = new Beneficio(5L, "NomeE", "DescE", BigDecimal.valueOf(99), true, 1L);

        BeneficioDTO dto = mapper.toBeneficioDTO(b);
        UpdateBeneficioDTO updateDto = mapper.toUpdateBeneficioDTO(b);

        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(b.getId());
        assertThat(dto.name()).isEqualTo(b.getName());
        assertThat(dto.description()).isEqualTo(b.getDescription());
        assertThat(dto.value()).isEqualByComparingTo(b.getValue());
        assertThat(dto.active()).isEqualTo(b.getActive());

        assertThat(updateDto).isNotNull();
        assertThat(updateDto.id()).isEqualTo(b.getId());
        assertThat(updateDto.name()).isEqualTo(b.getName());
        assertThat(updateDto.description()).isEqualTo(b.getDescription());
        assertThat(updateDto.value()).isEqualByComparingTo(b.getValue());
        assertThat(updateDto.active()).isEqualTo(b.getActive());
    }
}