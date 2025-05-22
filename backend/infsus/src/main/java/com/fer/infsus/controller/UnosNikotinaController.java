package com.fer.infsus.controller;

import com.fer.infsus.dto.UnosNikotinaDTO;
import com.fer.infsus.model.UnosNikotina;
import com.fer.infsus.service.UnosNikotinaService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/unosi-nikotina")
@RequiredArgsConstructor
public class UnosNikotinaController {
    private final UnosNikotinaService unosNikotinaService;

    @GetMapping
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
        // Korisnik i Proizvod bi se trebali dohvatiti iz baze po id-u, ovo je pojednostavljeno
        return u;
    }
}
