package com.fer.infsus.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fer.infsus.dto.UnosKupnjeDTO;
import com.fer.infsus.model.UnosKupnje;
import com.fer.infsus.service.UnosKupnjeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/unosi-kupnje")
@RequiredArgsConstructor
public class UnosKupnjeController {
    private final UnosKupnjeService unosKupnjeService;

    @GetMapping
    public List<UnosKupnjeDTO> sviUnosiKupnje() {
        return unosKupnjeService.sviUnosiKupnje().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UnosKupnjeDTO unosKupnjePoId(@PathVariable Integer id) {
        return unosKupnjeService.unosKupnjePoId(id).map(this::toDTO).orElse(null);
    }

    @PostMapping
    public UnosKupnjeDTO dodajUnosKupnje(@RequestBody UnosKupnjeDTO dto) {
        UnosKupnje unos = fromDTO(dto);
        return toDTO(unosKupnjeService.spremiUnosKupnje(unos));
    }

    @PutMapping("/{id}")
    public UnosKupnjeDTO azurirajUnosKupnje(@PathVariable Integer id, @RequestBody UnosKupnjeDTO dto) {
        UnosKupnje unos = fromDTO(dto);
        unos.setIdKupnja(id);
        return toDTO(unosKupnjeService.spremiUnosKupnje(unos));
    }

    @DeleteMapping("/{id}")
    public void obrisiUnosKupnje(@PathVariable Integer id) {
        unosKupnjeService.obrisiUnosKupnje(id);
    }

    @GetMapping("/korisnik/{idKorisnik}")
    public List<UnosKupnjeDTO> unosiKupnjeZaKorisnikaURasponu(
            @PathVariable Integer idKorisnik,
            @RequestParam("od") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime od,
            @RequestParam("do") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime doVremena) {
        return unosKupnjeService.unosiKupnjeZaKorisnikaURasponu(idKorisnik, od, doVremena)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    private UnosKupnjeDTO toDTO(UnosKupnje u) {
        UnosKupnjeDTO dto = new UnosKupnjeDTO();
        dto.setIdKupnja(u.getIdKupnja());
        dto.setKolicina(u.getKolicina());
        dto.setCijena(u.getCijena());
        dto.setIdKorisnik(u.getKorisnik() != null ? u.getKorisnik().getIdKorisnik() : null);
        dto.setIdProizvod(u.getProizvod() != null ? u.getProizvod().getIdProizvod() : null);
        dto.setDatum(u.getDatum());
        return dto;
    }
    private UnosKupnje fromDTO(UnosKupnjeDTO dto) {
        UnosKupnje u = new UnosKupnje();
        u.setIdKupnja(dto.getIdKupnja());
        u.setKolicina(dto.getKolicina());
        u.setCijena(dto.getCijena());
        return u;
    }
}
