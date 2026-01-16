package com.example.backend.service;


import com.example.backend.dto.BeneficioDTO;
import com.example.backend.dto.UpdateBeneficioDTO;

import java.util.List;

public interface BeneficioService {
    List<BeneficioDTO> getAll();

    BeneficioDTO getOne(Long id);

    BeneficioDTO create(BeneficioDTO beneficio);

    BeneficioDTO update(UpdateBeneficioDTO beneficio);

    BeneficioDTO partialUpdate(UpdateBeneficioDTO beneficio);

    void delete(Long id);
}
