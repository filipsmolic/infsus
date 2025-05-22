package com.fer.infsus.controller;

import com.fer.infsus.dto.ProizvodDTO;
import com.fer.infsus.mapper.ProizvodMapper;
import com.fer.infsus.model.Proizvod;
import com.fer.infsus.service.ProizvodService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/proizvodi")
public class ProizvodController {
    private final ProizvodService proizvodService;
    private final ProizvodMapper proizvodMapper;

    public ProizvodController(ProizvodService proizvodService, ProizvodMapper proizvodMapper) {
        this.proizvodService = proizvodService;
        this.proizvodMapper = proizvodMapper;
    }

    @GetMapping(produces = "application/json")
    public List<ProizvodDTO> sviProizvodi() {
        return proizvodService.sviProizvodi().stream().map(proizvodMapper::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ProizvodDTO proizvodPoId(@PathVariable Integer id) {
        return proizvodService.proizvodPoId(id).map(proizvodMapper::toDTO).orElse(null);
    }

    @PostMapping
    public ProizvodDTO dodajProizvod(@RequestBody ProizvodDTO dto) {
        Proizvod proizvod = proizvodMapper.fromDTO(dto);
        return proizvodMapper.toDTO(proizvodService.spremiProizvod(proizvod));
    }

    @PutMapping("/{id}")
    public ProizvodDTO azurirajProizvod(@PathVariable Integer id, @RequestBody ProizvodDTO dto) {
        Proizvod proizvod = proizvodMapper.fromDTO(dto);
        proizvod.setIdProizvod(id);
        return proizvodMapper.toDTO(proizvodService.spremiProizvod(proizvod));
    }

    @DeleteMapping("/{id}")
    public void obrisiProizvod(@PathVariable Integer id) {
        proizvodService.obrisiProizvod(id);
    }
}
