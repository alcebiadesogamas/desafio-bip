package com.example.backend.controller;

import com.example.backend.dto.BeneficioDTO;
import com.example.backend.dto.TransferDTO;
import com.example.backend.dto.UpdateBeneficioDTO;
import com.example.backend.service.BeneficioService;
import com.example.ejb.service.BeneficioEjbService;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/beneficios")
public class BeneficioController {

    private final BeneficioService beneficioService;
    private final BeneficioEjbService ejbService;

    public BeneficioController(BeneficioService beneficioService, BeneficioEjbService ejbService) {
        this.beneficioService = beneficioService;
        this.ejbService = ejbService;
    }

    @GetMapping
    public List<BeneficioDTO> list() {
        return beneficioService.getAll();
    }

    @GetMapping("/{id}")
    public BeneficioDTO getOne(@PathVariable Long id) {
        return beneficioService.getOne(id);
    }
    
    @PostMapping
    public BeneficioDTO create(@RequestBody BeneficioDTO beneficio) {
        return beneficioService.create(beneficio);
    }

    @PutMapping
    public BeneficioDTO update(@RequestBody UpdateBeneficioDTO beneficio) {
        return beneficioService.update(beneficio);
    }

    @PatchMapping
    public BeneficioDTO partialUpdate(@RequestBody UpdateBeneficioDTO beneficio) {
        return beneficioService.partialUpdate(beneficio);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        beneficioService.delete(id);
    }

    @PostMapping("/transfer")
    public void transfer(@RequestBody TransferDTO transferDTO) {
        ejbService.transfer(transferDTO.fromId(), transferDTO.toId(), transferDTO.amount());
    }

}
