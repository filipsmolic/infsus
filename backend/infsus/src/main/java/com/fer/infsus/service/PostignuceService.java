package com.fer.infsus.service;

import com.fer.infsus.model.Postignuce;
import com.fer.infsus.repository.PostignuceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostignuceService {
    private final PostignuceRepository postignuceRepository;

    public List<Postignuce> svaPostignuca() {
        return postignuceRepository.findAll();
    }

    public Optional<Postignuce> postignucePoId(Integer id) {
        return postignuceRepository.findById(id);
    }

    public Postignuce spremiPostignuce(Postignuce postignuce) {
        return postignuceRepository.save(postignuce);
    }

    public void obrisiPostignuce(Integer id) {
        postignuceRepository.deleteById(id);
    }
}
