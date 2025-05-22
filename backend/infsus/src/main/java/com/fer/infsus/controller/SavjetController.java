package com.fer.infsus.controller;

import com.fer.infsus.dto.SavjetDTO;
import com.fer.infsus.model.Savjet;
import com.fer.infsus.service.SavjetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/savjeti")
@RequiredArgsConstructor
public class SavjetController {
    private final SavjetService savjetService;

    @GetMapping
    public List<SavjetDTO> sviSavjeti() {
        return savjetService.sviSavjeti().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public SavjetDTO savjetPoId(@PathVariable Integer id) {
        return savjetService.savjetPoId(id).map(this::toDTO).orElse(null);
    }

    @PostMapping
    public SavjetDTO dodajSavjet(@RequestBody SavjetDTO dto) {
        Savjet savjet = fromDTO(dto);
        return toDTO(savjetService.spremiSavjet(savjet));
    }

    @PutMapping("/{id}")
    public SavjetDTO azurirajSavjet(@PathVariable Integer id, @RequestBody SavjetDTO dto) {
        Savjet savjet = fromDTO(dto);
        savjet.setIdSavjet(id);
        return toDTO(savjetService.spremiSavjet(savjet));
    }

    @DeleteMapping("/{id}")
    public void obrisiSavjet(@PathVariable Integer id) {
        savjetService.obrisiSavjet(id);
    }

    @GetMapping("/korisnik/{idKorisnik}")
    public List<SavjetDTO> sviSavjetiZaKorisnika(@PathVariable Integer idKorisnik) {
        return savjetService.sviSavjetiZaKorisnika(idKorisnik)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    private SavjetDTO toDTO(Savjet s) {
        SavjetDTO dto = new SavjetDTO();
        dto.setIdSavjet(s.getIdSavjet());
        dto.setIzvor(s.getIzvor());
        dto.setTekst(s.getTekst());
        dto.setIdKorisnik(s.getKorisnik() != null ? s.getKorisnik().getIdKorisnik() : null);
        return dto;
    }
    private Savjet fromDTO(SavjetDTO dto) {
        Savjet s = new Savjet();
        s.setIdSavjet(dto.getIdSavjet());
        s.setIzvor(dto.getIzvor());
        s.setTekst(dto.getTekst());
        // Korisnik bi se trebao dohvatiti iz baze po id-u, ovo je pojednostavljeno
        return s;
    }
}
