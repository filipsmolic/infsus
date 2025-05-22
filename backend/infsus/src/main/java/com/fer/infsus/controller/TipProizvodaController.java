package com.fer.infsus.controller;

import com.fer.infsus.dto.TipProizvodaDTO;
import com.fer.infsus.mapper.TipProizvodaMapper;
import com.fer.infsus.model.TipProizvoda;
import com.fer.infsus.service.TipProizvodaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tipovi-proizvoda")
public class TipProizvodaController {
    private final TipProizvodaService tipProizvodaService;
    private final TipProizvodaMapper tipProizvodaMapper;

    public TipProizvodaController(TipProizvodaService tipProizvodaService, TipProizvodaMapper tipProizvodaMapper) {
        this.tipProizvodaService = tipProizvodaService;
        this.tipProizvodaMapper = tipProizvodaMapper;
    }

    @GetMapping(produces = "application/json")
    public List<TipProizvodaDTO> sviTipovi() {
        return tipProizvodaService.sviTipovi().stream().map(tipProizvodaMapper::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public TipProizvodaDTO tipPoId(@PathVariable Integer id) {
        return tipProizvodaService.tipPoId(id).map(tipProizvodaMapper::toDTO).orElse(null);
    }

    @PostMapping
    public TipProizvodaDTO dodajTip(@RequestBody TipProizvodaDTO dto) {
        TipProizvoda tip = tipProizvodaMapper.fromDTO(dto);
        return tipProizvodaMapper.toDTO(tipProizvodaService.spremiTip(tip));
    }

    @PutMapping("/{id}")
    public TipProizvodaDTO azurirajTip(@PathVariable Integer id, @RequestBody TipProizvodaDTO dto) {
        TipProizvoda tip = tipProizvodaMapper.fromDTO(dto);
        tip.setIdTipProizvoda(id);
        return tipProizvodaMapper.toDTO(tipProizvodaService.spremiTip(tip));
    }

    @DeleteMapping("/{id}")
    public void obrisiTip(@PathVariable Integer id) {
        tipProizvodaService.obrisiTip(id);
    }
}
