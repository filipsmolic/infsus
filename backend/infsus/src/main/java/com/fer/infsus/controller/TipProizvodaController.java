package com.fer.infsus.controller;

import com.fer.infsus.dto.TipProizvodaDTO;
import com.fer.infsus.model.TipProizvoda;
import com.fer.infsus.service.TipProizvodaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tipovi-proizvoda")
@RequiredArgsConstructor
public class TipProizvodaController {
    private final TipProizvodaService tipProizvodaService;

    @GetMapping
    public List<TipProizvodaDTO> sviTipovi() {
        return tipProizvodaService.sviTipovi().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public TipProizvodaDTO tipPoId(@PathVariable Integer id) {
        return tipProizvodaService.tipPoId(id).map(this::toDTO).orElse(null);
    }

    @PostMapping
    public TipProizvodaDTO dodajTip(@RequestBody TipProizvodaDTO dto) {
        TipProizvoda tip = fromDTO(dto);
        return toDTO(tipProizvodaService.spremiTip(tip));
    }

    @PutMapping("/{id}")
    public TipProizvodaDTO azurirajTip(@PathVariable Integer id, @RequestBody TipProizvodaDTO dto) {
        TipProizvoda tip = fromDTO(dto);
        tip.setIdTipProizvoda(id);
        return toDTO(tipProizvodaService.spremiTip(tip));
    }

    @DeleteMapping("/{id}")
    public void obrisiTip(@PathVariable Integer id) {
        tipProizvodaService.obrisiTip(id);
    }

    private TipProizvodaDTO toDTO(TipProizvoda t) {
        TipProizvodaDTO dto = new TipProizvodaDTO();
        dto.setIdTipProizvoda(t.getIdTipProizvoda());
        dto.setNaziv(t.getNaziv());
        return dto;
    }
    private TipProizvoda fromDTO(TipProizvodaDTO dto) {
        TipProizvoda t = new TipProizvoda();
        t.setIdTipProizvoda(dto.getIdTipProizvoda());
        t.setNaziv(dto.getNaziv());
        return t;
    }
}
