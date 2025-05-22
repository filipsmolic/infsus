package com.fer.infsus.service;

import com.fer.infsus.model.Proizvod;
import com.fer.infsus.repository.ProizvodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProizvodService {
    private final ProizvodRepository proizvodRepository;

    public List<Proizvod> sviProizvodi() {
        return proizvodRepository.findAll();
    }

    public Optional<Proizvod> proizvodPoId(Integer id) {
        return proizvodRepository.findById(id);
    }

    public Proizvod spremiProizvod(Proizvod proizvod) {
        return proizvodRepository.save(proizvod);
    }

    public void obrisiProizvod(Integer id) {
        proizvodRepository.deleteById(id);
    }
}
