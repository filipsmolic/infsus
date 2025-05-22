package com.fer.infsus.controller;

import com.fer.infsus.dto.ProizvodDTO;
import com.fer.infsus.model.Proizvod;
import com.fer.infsus.service.ProizvodService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/proizvodi")
@RequiredArgsConstructor
public class ProizvodController {
    private final ProizvodService proizvodService;

    @GetMapping
    public List<ProizvodDTO> sviProizvodi() {
        return proizvodService.sviProizvodi().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ProizvodDTO proizvodPoId(@PathVariable Integer id) {
        return proizvodService.proizvodPoId(id).map(this::toDTO).orElse(null);
    }

    @PostMapping
    public ProizvodDTO dodajProizvod(@RequestBody ProizvodDTO dto) {
        Proizvod proizvod = fromDTO(dto);
        return toDTO(proizvodService.spremiProizvod(proizvod));
    }

    @PutMapping("/{id}")
    public ProizvodDTO azurirajProizvod(@PathVariable Integer id, @RequestBody ProizvodDTO dto) {
        Proizvod proizvod = fromDTO(dto);
        proizvod.setIdProizvod(id);
        return toDTO(proizvodService.spremiProizvod(proizvod));
    }

    @DeleteMapping("/{id}")
    public void obrisiProizvod(@PathVariable Integer id) {
        proizvodService.obrisiProizvod(id);
    }

    private ProizvodDTO toDTO(Proizvod p) {
        ProizvodDTO dto = new ProizvodDTO();
        dto.setIdProizvod(p.getIdProizvod());
        dto.setOpis(p.getOpis());
        dto.setNikotinSadrzaj(p.getNikotinSadrzaj());
        dto.setIdTipProizvoda(p.getTipProizvoda() != null ? p.getTipProizvoda().getIdTipProizvoda() : null);
        return dto;
    }
    private Proizvod fromDTO(ProizvodDTO dto) {
        Proizvod p = new Proizvod();
        p.setIdProizvod(dto.getIdProizvod());
        p.setOpis(dto.getOpis());
        p.setNikotinSadrzaj(dto.getNikotinSadrzaj());
        // TipProizvoda bi se trebao dohvatiti iz baze po id-u, ovo je pojednostavljeno
        return p;
    }
}
