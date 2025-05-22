package com.fer.infsus.controller;

import com.fer.infsus.dto.BatchUnosNikotinaDTO;
import com.fer.infsus.dto.UnosNikotinaDTO;
import com.fer.infsus.dto.UnosiZaKorisnikaURasponuDTO;
import com.fer.infsus.mapper.UnosNikotinaMapper;
import com.fer.infsus.model.Korisnik;
import com.fer.infsus.model.Proizvod;
import com.fer.infsus.model.UnosNikotina;
import com.fer.infsus.repository.KorisnikRepository;
import com.fer.infsus.repository.ProizvodRepository;
import com.fer.infsus.service.UnosNikotinaService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/unosi-nikotina")

public class UnosNikotinaController {
    private final UnosNikotinaService unosNikotinaService;
    private final KorisnikRepository korisnikRepository;
    private final ProizvodRepository proizvodRepository;
    private final UnosNikotinaMapper unosNikotinaMapper;

    public UnosNikotinaController(UnosNikotinaService unosNikotinaService, 
                                   KorisnikRepository korisnikRepository,
                                   ProizvodRepository proizvodRepository,
                                   UnosNikotinaMapper unosNikotinaMapper) {
        this.unosNikotinaService = unosNikotinaService;
        this.korisnikRepository = korisnikRepository;
        this.proizvodRepository = proizvodRepository;
        this.unosNikotinaMapper = unosNikotinaMapper;
    }

    @GetMapping(produces = "application/json")
    public List<UnosNikotinaDTO> sviUnosiNikotina() {
        return unosNikotinaService.sviUnosiNikotina().stream().map(unosNikotinaMapper::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UnosNikotinaDTO unosNikotinaPoId(@PathVariable Integer id) {
        return unosNikotinaService.unosNikotinaPoId(id).map(unosNikotinaMapper::toDTO).orElse(null);
    }

    @PostMapping
    public UnosNikotinaDTO dodajUnosNikotina(@RequestBody UnosNikotinaDTO dto) {
        UnosNikotina unos = unosNikotinaMapper.fromDTO(dto);
        return unosNikotinaMapper.toDTO(unosNikotinaService.spremiUnosNikotina(unos));
    }

    @PutMapping("/{id}")
    public UnosNikotinaDTO azurirajUnosNikotina(@PathVariable Integer id, @RequestBody UnosNikotinaDTO dto) {
        UnosNikotina unos = unosNikotinaMapper.fromDTO(dto);
        unos.setIdUnosNikotina(id);
        return unosNikotinaMapper.toDTO(unosNikotinaService.spremiUnosNikotina(unos));
    }

    @DeleteMapping("/{id}")
    public void obrisiUnosNikotina(@PathVariable Integer id) {
        unosNikotinaService.obrisiUnosNikotina(id);
    }

    @GetMapping(value = "/korisnik/{idKorisnik}", produces = "application/json")
    public List<UnosiZaKorisnikaURasponuDTO> unosiZaKorisnikaURasponu(
            @PathVariable Integer idKorisnik,
            @RequestParam("od") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime od,
            @RequestParam("do") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime doVremena) {
        return unosNikotinaService.unosiZaKorisnikaURasponu(idKorisnik, od, doVremena)
                .stream().map(unosNikotinaMapper::toUnosiZaKorisnikaURasponuDTO).collect(Collectors.toList());
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
            return unosNikotinaMapper.toDTO(unosNikotinaService.spremiUnosNikotina(unos));
        }).collect(Collectors.toList());
    }
}
