package com.fer.infsus.controller;

import com.fer.infsus.dto.PostotnoPostignuceDTO;
import com.fer.infsus.model.PostotnoPostignuce;
import com.fer.infsus.service.PostotnoPostignuceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/postotna-postignuca")
@RequiredArgsConstructor
public class PostotnoPostignuceController {
    private final PostotnoPostignuceService postotnoPostignuceService;

    @GetMapping
    public List<PostotnoPostignuceDTO> svaPostotnaPostignuca() {
        return postotnoPostignuceService.svaPostotnaPostignuca().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public PostotnoPostignuceDTO postotnoPostignucePoId(@PathVariable Integer id) {
        return postotnoPostignuceService.postotnoPostignucePoId(id).map(this::toDTO).orElse(null);
    }

    @PostMapping
    public PostotnoPostignuceDTO dodajPostotnoPostignuce(@RequestBody PostotnoPostignuceDTO dto) {
        PostotnoPostignuce postotnoPostignuce = fromDTO(dto);
        return toDTO(postotnoPostignuceService.spremiPostotnoPostignuce(postotnoPostignuce));
    }

    @PutMapping("/{id}")
    public PostotnoPostignuceDTO azurirajPostotnoPostignuce(@PathVariable Integer id, @RequestBody PostotnoPostignuceDTO dto) {
        PostotnoPostignuce postotnoPostignuce = fromDTO(dto);
        postotnoPostignuce.setIdPostignuce(id);
        return toDTO(postotnoPostignuceService.spremiPostotnoPostignuce(postotnoPostignuce));
    }

    @DeleteMapping("/{id}")
    public void obrisiPostotnoPostignuce(@PathVariable Integer id) {
        postotnoPostignuceService.obrisiPostotnoPostignuce(id);
    }

    private PostotnoPostignuceDTO toDTO(PostotnoPostignuce p) {
        PostotnoPostignuceDTO dto = new PostotnoPostignuceDTO();
        dto.setIdPostignuce(p.getIdPostignuce());
        dto.setPostotakUvjet(p.getPostotakUvjet());
        return dto;
    }
    private PostotnoPostignuce fromDTO(PostotnoPostignuceDTO dto) {
        PostotnoPostignuce p = new PostotnoPostignuce();
        p.setIdPostignuce(dto.getIdPostignuce());
        p.setPostotakUvjet(dto.getPostotakUvjet());
        return p;
    }
}
