package com.fer.infsus.controller;

import com.fer.infsus.dto.PostignuceDTO;
import com.fer.infsus.model.Postignuce;
import com.fer.infsus.service.PostignuceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/postignuca")
@RequiredArgsConstructor
public class PostignuceController {
    private final PostignuceService postignuceService;

    @GetMapping
    public List<PostignuceDTO> svaPostignuca() {
        return postignuceService.svaPostignuca().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public PostignuceDTO postignucePoId(@PathVariable Integer id) {
        return postignuceService.postignucePoId(id).map(this::toDTO).orElse(null);
    }

    @PostMapping
    public PostignuceDTO dodajPostignuce(@RequestBody PostignuceDTO dto) {
        Postignuce postignuce = fromDTO(dto);
        return toDTO(postignuceService.spremiPostignuce(postignuce));
    }

    @PutMapping("/{id}")
    public PostignuceDTO azurirajPostignuce(@PathVariable Integer id, @RequestBody PostignuceDTO dto) {
        Postignuce postignuce = fromDTO(dto);
        postignuce.setIdPostignuce(id);
        return toDTO(postignuceService.spremiPostignuce(postignuce));
    }

    @DeleteMapping("/{id}")
    public void obrisiPostignuce(@PathVariable Integer id) {
        postignuceService.obrisiPostignuce(id);
    }

    private PostignuceDTO toDTO(Postignuce p) {
        PostignuceDTO dto = new PostignuceDTO();
        dto.setIdPostignuce(p.getIdPostignuce());
        dto.setNaziv(p.getNaziv());
        dto.setOpis(p.getOpis());
        return dto;
    }
    private Postignuce fromDTO(PostignuceDTO dto) {
        Postignuce p = new Postignuce();
        p.setIdPostignuce(dto.getIdPostignuce());
        p.setNaziv(dto.getNaziv());
        p.setOpis(dto.getOpis());
        return p;
    }
}
