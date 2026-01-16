package com.example.backend.service.mapper;

import com.example.backend.dto.BeneficioDTO;
import com.example.backend.dto.UpdateBeneficioDTO;
import com.example.ejb.model.Beneficio;

public class BeneficioMapper {

    private BeneficioMapper(){}

    public static BeneficioMapper getInstance() {
        return new BeneficioMapper();
    }
    public Beneficio toBeneficio(BeneficioDTO dto) {
        return new Beneficio(null, dto.name(), dto.description(), dto.value(), dto.active(), null);
    }

    public Beneficio toBeneficio(UpdateBeneficioDTO dto) {
        return new Beneficio(null, dto.name(), dto.description(), dto.value(), dto.active(), null);
    }

    public BeneficioDTO toBeneficioDTO(Beneficio beneficio) {
        return new BeneficioDTO(beneficio.getId(), beneficio.getName(), beneficio.getDescription(), beneficio.getValue(), beneficio.getActive());
    }

    public UpdateBeneficioDTO toUpdateBeneficioDTO(Beneficio beneficio) {
        return new UpdateBeneficioDTO(beneficio.getId(), beneficio.getName(), beneficio.getDescription(), beneficio.getValue(), beneficio.getActive());
    }

}
