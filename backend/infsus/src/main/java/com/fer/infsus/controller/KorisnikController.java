package com.fer.infsus.controller;

import com.fer.infsus.dto.KorisnikDTO;
import com.fer.infsus.model.Korisnik;
import com.fer.infsus.service.KorisnikService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/korisnici")
@RequiredArgsConstructor
public class KorisnikController {
    private final KorisnikService korisnikService;

    @GetMapping
    public List<KorisnikDTO> sviKorisnici() {
        return korisnikService.sviKorisnici().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public KorisnikDTO korisnikPoId(@PathVariable Integer id) {
        return korisnikService.korisnikPoId(id).map(this::toDTO).orElse(null);
    }

    @PostMapping
    public KorisnikDTO dodajKorisnika(@RequestBody KorisnikDTO dto) {
        Korisnik korisnik = fromDTO(dto);
        return toDTO(korisnikService.spremiKorisnika(korisnik));
    }

    @PutMapping("/{id}")
    public KorisnikDTO azurirajKorisnika(@PathVariable Integer id, @RequestBody KorisnikDTO dto) {
        Korisnik korisnik = fromDTO(dto);
        korisnik.setIdKorisnik(id);
        return toDTO(korisnikService.spremiKorisnika(korisnik));
    }

    @DeleteMapping("/{id}")
    public void obrisiKorisnika(@PathVariable Integer id) {
        korisnikService.obrisiKorisnika(id);
    }

    private KorisnikDTO toDTO(Korisnik k) {
        KorisnikDTO dto = new KorisnikDTO();
        dto.setIdKorisnik(k.getIdKorisnik());
        dto.setIme(k.getIme());
        dto.setEmail(k.getEmail());
        dto.setUloga(k.getUloga());
        return dto;
    }
    private Korisnik fromDTO(KorisnikDTO dto) {
        Korisnik k = new Korisnik();
        k.setIdKorisnik(dto.getIdKorisnik());
        k.setIme(dto.getIme());
        k.setEmail(dto.getEmail());
        k.setUloga(dto.getUloga());
        return k;
    }
}
