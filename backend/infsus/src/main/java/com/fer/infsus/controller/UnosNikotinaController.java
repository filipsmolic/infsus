package com.fer.infsus.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
        if (dto.getDatum() != null && dto.getDatum().toLocalDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Datum unosa ne može biti u budućnosti.");
        }
        UnosNikotina unos = unosNikotinaMapper.fromDTO(dto);
        return unosNikotinaMapper.toDTO(unosNikotinaService.spremiUnosNikotina(unos));
    }

    @PutMapping("/{id}")
    public UnosNikotinaDTO azurirajUnosNikotina(@PathVariable Integer id, @RequestBody UnosNikotinaDTO dto) {
        if (dto.getDatum() != null && dto.getDatum().toLocalDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Datum unosa ne može biti u budućnosti.");
        }
        UnosNikotina unos = unosNikotinaMapper.fromDTO(dto);
        unos.setIdUnosNikotina(id);
        return unosNikotinaMapper.toDTO(unosNikotinaService.spremiUnosNikotina(unos));
    }

    @DeleteMapping("/{id}")
    public void obrisiUnosNikotina(@PathVariable Integer id) {
        unosNikotinaService.obrisiUnosNikotina(id);
    }

    @GetMapping(value = "/korisnik/{idKorisnik}", produces = "application/json")
    public Page<UnosiZaKorisnikaURasponuDTO> unosiZaKorisnikaURasponu(
            @PathVariable Integer idKorisnik,
            @RequestParam("od") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime od,
            @RequestParam("do") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime doVremena,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "datum"));
        
        return unosNikotinaService.unosiZaKorisnikaURasponu(idKorisnik, od, doVremena, pageable)
                .map(unosNikotinaMapper::toUnosiZaKorisnikaURasponuDTO);
    }

    @PostMapping("/batch")
    public List<UnosNikotinaDTO> batchUnosNikotina(@RequestBody BatchUnosNikotinaDTO batchDto) {
        if (batchDto.getDatum() != null && batchDto.getDatum().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Datum unosa ne može biti u budućnosti.");
        }

        Korisnik korisnik = korisnikRepository.findById(batchDto.getIdKorisnik())
            .orElseThrow(() -> new IllegalArgumentException("Korisnik s ID-om " + batchDto.getIdKorisnik() + " nije pronađen."));
        
        return batchDto.getProizvodi().stream().map(p -> {
            Proizvod proizvod = proizvodRepository.findById(p.getIdProizvod())
                .orElseThrow(() -> new IllegalArgumentException("Proizvod s ID-om " + p.getIdProizvod() + " nije pronađen."));
            
            UnosNikotina unos = new UnosNikotina();
            unos.setKolicina(p.getKolicina());
            if (batchDto.getDatum() != null) {
                unos.setDatum(batchDto.getDatum().atStartOfDay()); 
            }
            unos.setKorisnik(korisnik);
            unos.setProizvod(proizvod);
            return unosNikotinaMapper.toDTO(unosNikotinaService.spremiUnosNikotina(unos));
        }).collect(Collectors.toList());
    }
}

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
