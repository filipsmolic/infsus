package com.fer.infsus.controller;

import com.fer.infsus.dto.BatchUnosNikotinaDTO;
import com.fer.infsus.dto.UnosNikotinaDTO;
import com.fer.infsus.model.Korisnik;
import com.fer.infsus.model.Proizvod;
import com.fer.infsus.model.UnosNikotina;
import com.fer.infsus.repository.KorisnikRepository;
import com.fer.infsus.repository.ProizvodRepository;
import com.fer.infsus.service.UnosNikotinaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/unosi-nikotina")

public class UnosNikotinaController {
    private final UnosNikotinaService unosNikotinaService;

    //@Autowired
    private KorisnikRepository korisnikRepository;
    //@Autowired
    private ProizvodRepository proizvodRepository;

    public UnosNikotinaController(UnosNikotinaService unosNikotinaService, 
                                   KorisnikRepository korisnikRepository,
                                   ProizvodRepository proizvodRepository) {
        this.unosNikotinaService = unosNikotinaService;
        this.korisnikRepository = korisnikRepository;
        this.proizvodRepository = proizvodRepository;
    }

    @GetMapping(produces = "application/json")
    public List<UnosNikotinaDTO> sviUnosiNikotina() {
        return unosNikotinaService.sviUnosiNikotina().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UnosNikotinaDTO unosNikotinaPoId(@PathVariable Integer id) {
        return unosNikotinaService.unosNikotinaPoId(id).map(this::toDTO).orElse(null);
    }

    @PostMapping
    public UnosNikotinaDTO dodajUnosNikotina(@RequestBody UnosNikotinaDTO dto) {
        UnosNikotina unos = fromDTO(dto);
        return toDTO(unosNikotinaService.spremiUnosNikotina(unos));
    }

    @PutMapping("/{id}")
    public UnosNikotinaDTO azurirajUnosNikotina(@PathVariable Integer id, @RequestBody UnosNikotinaDTO dto) {
        UnosNikotina unos = fromDTO(dto);
        unos.setIdUnosNikotina(id);
        return toDTO(unosNikotinaService.spremiUnosNikotina(unos));
    }

    @DeleteMapping("/{id}")
    public void obrisiUnosNikotina(@PathVariable Integer id) {
        unosNikotinaService.obrisiUnosNikotina(id);
    }

    @GetMapping("/korisnik/{idKorisnik}")
    public List<UnosNikotinaDTO> unosiZaKorisnikaURasponu(
            @PathVariable Integer idKorisnik,
            @RequestParam("od") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime od,
            @RequestParam("do") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime doVremena) {
        return unosNikotinaService.unosiZaKorisnikaURasponu(idKorisnik, od, doVremena)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @PostMapping("/batch")
    public List<UnosNikotinaDTO> batchUnosNikotina(@RequestBody BatchUnosNikotinaDTO batchDto) {
        Korisnik korisnik = korisnikRepository.findById(batchDto.getIdKorisnik()).orElseThrow();
        return batchDto.getProizvodi().stream().map(p -> {
            Proizvod proizvod = proizvodRepository.findById(p.getIdProizvod()).orElseThrow();
            UnosNikotina unos = new UnosNikotina();
            unos.setKolicina(p.getKolicina());
            unos.setDatum(batchDto.getDatum().atStartOfDay());
            unos.setKorisnik(korisnik);
            unos.setProizvod(proizvod);
            return toDTO(unosNikotinaService.spremiUnosNikotina(unos));
        }).collect(Collectors.toList());
    }

    private UnosNikotinaDTO toDTO(UnosNikotina u) {
        UnosNikotinaDTO dto = new UnosNikotinaDTO();
        dto.setIdUnosNikotina(u.getIdUnosNikotina());
        dto.setKolicina(u.getKolicina());
        dto.setIdKorisnik(u.getKorisnik() != null ? u.getKorisnik().getIdKorisnik() : null);
        dto.setIdProizvod(u.getProizvod() != null ? u.getProizvod().getIdProizvod() : null);
        return dto;
    }

    private UnosNikotina fromDTO(UnosNikotinaDTO dto) {
        UnosNikotina u = new UnosNikotina();
        u.setIdUnosNikotina(dto.getIdUnosNikotina());
        u.setKolicina(dto.getKolicina());
        if (dto.getIdKorisnik() != null) {
            u.setKorisnik(korisnikRepository.findById(dto.getIdKorisnik()).orElse(null));
        }
        if (dto.getIdProizvod() != null) {
            u.setProizvod(proizvodRepository.findById(dto.getIdProizvod()).orElse(null));
        }
        return u;
    }
}
