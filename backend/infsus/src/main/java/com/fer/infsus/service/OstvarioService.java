package com.fer.infsus.service;

import com.fer.infsus.model.Ostvario;
import com.fer.infsus.model.OstvarioId;
import com.fer.infsus.repository.OstvarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OstvarioService {
    private final OstvarioRepository ostvarioRepository;

    public List<Ostvario> sviOstvareni() {
        return ostvarioRepository.findAll();
    }

    public Optional<Ostvario> ostvarenPoId(OstvarioId id) {
        return ostvarioRepository.findById(id);
    }

    public Ostvario spremiOstvarenje(Ostvario ostvario) {
        return ostvarioRepository.save(ostvario);
    }

    public void obrisiOstvarenje(OstvarioId id) {
        ostvarioRepository.deleteById(id);
    }
}
