package com.fer.infsus.controller;

import com.fer.infsus.dto.VremenskoPostignuceDTO;
import com.fer.infsus.model.VremenskoPostignuce;
import com.fer.infsus.service.VremenskoPostignuceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/vremenska-postignuca")
@RequiredArgsConstructor
public class VremenskoPostignuceController {
    private final VremenskoPostignuceService vremenskoPostignuceService;

    @GetMapping
    public List<VremenskoPostignuceDTO> svaVremenskaPostignuca() {
        return vremenskoPostignuceService.svaVremenskaPostignuca().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public VremenskoPostignuceDTO vremenskoPostignucePoId(@PathVariable Integer id) {
        return vremenskoPostignuceService.vremenskoPostignucePoId(id).map(this::toDTO).orElse(null);
    }

    @PostMapping
    public VremenskoPostignuceDTO dodajVremenskoPostignuce(@RequestBody VremenskoPostignuceDTO dto) {
        VremenskoPostignuce vremenskoPostignuce = fromDTO(dto);
        return toDTO(vremenskoPostignuceService.spremiVremenskoPostignuce(vremenskoPostignuce));
    }

    @PutMapping("/{id}")
    public VremenskoPostignuceDTO azurirajVremenskoPostignuce(@PathVariable Integer id, @RequestBody VremenskoPostignuceDTO dto) {
        VremenskoPostignuce vremenskoPostignuce = fromDTO(dto);
        vremenskoPostignuce.setIdPostignuce(id);
        return toDTO(vremenskoPostignuceService.spremiVremenskoPostignuce(vremenskoPostignuce));
    }

    @DeleteMapping("/{id}")
    public void obrisiVremenskoPostignuce(@PathVariable Integer id) {
        vremenskoPostignuceService.obrisiVremenskoPostignuce(id);
    }

    private VremenskoPostignuceDTO toDTO(VremenskoPostignuce p) {
        VremenskoPostignuceDTO dto = new VremenskoPostignuceDTO();
        dto.setIdPostignuce(p.getIdPostignuce());
        dto.setBrojDanaUvjet(p.getBrojDanaUvjet());
        return dto;
    }
    private VremenskoPostignuce fromDTO(VremenskoPostignuceDTO dto) {
        VremenskoPostignuce p = new VremenskoPostignuce();
        p.setIdPostignuce(dto.getIdPostignuce());
        p.setBrojDanaUvjet(dto.getBrojDanaUvjet());
        return p;
    }
}
