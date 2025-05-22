package com.fer.infsus.controller;

import com.fer.infsus.dto.OstvarioDTO;
import com.fer.infsus.model.Ostvario;
import com.fer.infsus.model.OstvarioId;
import com.fer.infsus.service.OstvarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ostvareni")
@RequiredArgsConstructor
public class OstvarioController {
    private final OstvarioService ostvarioService;

    @GetMapping
    public List<OstvarioDTO> sviOstvareni() {
        return ostvarioService.sviOstvareni().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/{idKorisnik}/{idPostignuce}")
    public OstvarioDTO ostvarenPoId(@PathVariable Integer idKorisnik, @PathVariable Integer idPostignuce) {
        OstvarioId id = new OstvarioId();
        id.setIdKorisnik(idKorisnik);
        id.setIdPostignuce(idPostignuce);
        return ostvarioService.ostvarenPoId(id).map(this::toDTO).orElse(null);
    }

    @PostMapping
    public OstvarioDTO dodajOstvarenje(@RequestBody OstvarioDTO dto) {
        Ostvario ostvario = fromDTO(dto);
        return toDTO(ostvarioService.spremiOstvarenje(ostvario));
    }

    @DeleteMapping("/{idKorisnik}/{idPostignuce}")
    public void obrisiOstvarenje(@PathVariable Integer idKorisnik, @PathVariable Integer idPostignuce) {
        OstvarioId id = new OstvarioId();
        id.setIdKorisnik(idKorisnik);
        id.setIdPostignuce(idPostignuce);
        ostvarioService.obrisiOstvarenje(id);
    }

    private OstvarioDTO toDTO(Ostvario o) {
        OstvarioDTO dto = new OstvarioDTO();
        dto.setIdKorisnik(o.getIdKorisnik());
        dto.setIdPostignuce(o.getIdPostignuce());
        return dto;
    }
    private Ostvario fromDTO(OstvarioDTO dto) {
        Ostvario o = new Ostvario();
        o.setIdKorisnik(dto.getIdKorisnik());
        o.setIdPostignuce(dto.getIdPostignuce());
        return o;
    }
}
