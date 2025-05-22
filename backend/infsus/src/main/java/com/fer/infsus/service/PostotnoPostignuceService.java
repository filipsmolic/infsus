package com.fer.infsus.service;

import com.fer.infsus.model.PostotnoPostignuce;
import com.fer.infsus.repository.PostotnoPostignuceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostotnoPostignuceService {
    private final PostotnoPostignuceRepository postotnoPostignuceRepository;

    public List<PostotnoPostignuce> svaPostotnaPostignuca() {
        return postotnoPostignuceRepository.findAll();
    }

    public Optional<PostotnoPostignuce> postotnoPostignucePoId(Integer id) {
        return postotnoPostignuceRepository.findById(id);
    }

    public PostotnoPostignuce spremiPostotnoPostignuce(PostotnoPostignuce postotnoPostignuce) {
        return postotnoPostignuceRepository.save(postotnoPostignuce);
    }

    public void obrisiPostotnoPostignuce(Integer id) {
        postotnoPostignuceRepository.deleteById(id);
    }
}
