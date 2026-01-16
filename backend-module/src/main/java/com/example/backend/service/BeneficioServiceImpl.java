package com.example.backend.service;

import com.example.backend.dto.BeneficioDTO;
import com.example.backend.dto.UpdateBeneficioDTO;
import com.example.backend.repository.BeneficioRepository;
import com.example.backend.service.mapper.BeneficioMapper;
import com.example.ejb.service.exception.BeneficioNotFoundException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BeneficioServiceImpl implements BeneficioService{

    private final BeneficioRepository beneficioRepository;
    private final BeneficioMapper beneficioMapper;

    public BeneficioServiceImpl(BeneficioRepository beneficioRepository) {
        this.beneficioRepository = beneficioRepository;
        this.beneficioMapper = BeneficioMapper.getInstance();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BeneficioDTO> getAll() {
        return beneficioRepository.findAll().stream().map(beneficioMapper::toBeneficioDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BeneficioDTO getOne(Long id) {
        return beneficioRepository.findById(id).map(beneficioMapper::toBeneficioDTO).orElseThrow(() -> new BeneficioNotFoundException(id));
    }

    @Transactional
    @Override
    public BeneficioDTO create(BeneficioDTO dto) {
        var beneficio = beneficioMapper.toBeneficio(dto);
        return beneficioMapper.toBeneficioDTO(beneficioRepository.save(beneficio));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Modifying
    public BeneficioDTO update(UpdateBeneficioDTO dto) {
        var existingBeneficio = beneficioRepository.findById(dto.id()).orElseThrow(() -> new BeneficioNotFoundException(dto.id()));
        existingBeneficio.setName(dto.name());
        existingBeneficio.setDescription(dto.description());
        existingBeneficio.setValue(dto.value());
        existingBeneficio.setActive(dto.active());
        return beneficioMapper.toBeneficioDTO(beneficioRepository.save(existingBeneficio));
    }

    @Override
    @Modifying
    @Transactional
    public BeneficioDTO partialUpdate(UpdateBeneficioDTO beneficio) {
        var existingBeneficio = beneficioRepository.findById(beneficio.id()).orElseThrow(() -> new BeneficioNotFoundException(beneficio.id()));
        if (beneficio.name() != null) {
            existingBeneficio.setName(beneficio.name());
        }
        if (beneficio.description() != null) {
            existingBeneficio.setDescription(beneficio.description());
        }
        if (beneficio.value() != null) {
            existingBeneficio.setValue(beneficio.value());
        }
        if (beneficio.active() != null) {
            existingBeneficio.setActive(beneficio.active());
        }
        return beneficioMapper.toBeneficioDTO(beneficioRepository.save(existingBeneficio));
        
    }

    @Override
    @Modifying
    @Transactional
    public void delete(Long id) {
        if (!beneficioRepository.existsById(id)) {
            throw new BeneficioNotFoundException(id);
        }
        var benefit = beneficioRepository.findById(id).orElseThrow(() -> new BeneficioNotFoundException(id));
        beneficioRepository.delete(benefit);
    }
}
