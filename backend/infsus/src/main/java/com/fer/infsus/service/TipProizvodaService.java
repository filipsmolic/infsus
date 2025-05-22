package com.fer.infsus.service;

import com.fer.infsus.model.TipProizvoda;
import com.fer.infsus.repository.TipProizvodaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TipProizvodaService {
    private final TipProizvodaRepository tipProizvodaRepository;

    public List<TipProizvoda> sviTipovi() {
        return tipProizvodaRepository.findAll();
    }

    public Optional<TipProizvoda> tipPoId(Integer id) {
        return tipProizvodaRepository.findById(id);
    }

    public TipProizvoda spremiTip(TipProizvoda tip) {
        return tipProizvodaRepository.save(tip);
    }

    public void obrisiTip(Integer id) {
        tipProizvodaRepository.deleteById(id);
    }
}
