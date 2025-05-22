package com.fer.infsus.service;

import com.fer.infsus.model.VremenskoPostignuce;
import com.fer.infsus.repository.VremenskoPostignuceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VremenskoPostignuceService {
    private final VremenskoPostignuceRepository vremenskoPostignuceRepository;

    public List<VremenskoPostignuce> svaVremenskaPostignuca() {
        return vremenskoPostignuceRepository.findAll();
    }

    public Optional<VremenskoPostignuce> vremenskoPostignucePoId(Integer id) {
        return vremenskoPostignuceRepository.findById(id);
    }

    public VremenskoPostignuce spremiVremenskoPostignuce(VremenskoPostignuce vremenskoPostignuce) {
        return vremenskoPostignuceRepository.save(vremenskoPostignuce);
    }

    public void obrisiVremenskoPostignuce(Integer id) {
        vremenskoPostignuceRepository.deleteById(id);
    }
}
