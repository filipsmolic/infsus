package com.fer.infsus.service;

import com.fer.infsus.model.Savjet;
import com.fer.infsus.repository.SavjetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SavjetService {
    private final SavjetRepository savjetRepository;

    public List<Savjet> sviSavjeti() {
        return savjetRepository.findAll();
    }

    public Optional<Savjet> savjetPoId(Integer id) {
        return savjetRepository.findById(id);
    }

    public Savjet spremiSavjet(Savjet savjet) {
        return savjetRepository.save(savjet);
    }

    public void obrisiSavjet(Integer id) {
        savjetRepository.deleteById(id);
    }

    public List<Savjet> sviSavjetiZaKorisnika(Integer idKorisnik) {
        return savjetRepository.findByKorisnikIdKorisnik(idKorisnik);
    }
}
