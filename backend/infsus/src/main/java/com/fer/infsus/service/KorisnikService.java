package com.fer.infsus.service;

import com.fer.infsus.model.Korisnik;
import com.fer.infsus.repository.KorisnikRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KorisnikService {
    private final KorisnikRepository korisnikRepository;

    public List<Korisnik> sviKorisnici() {
        return korisnikRepository.findAll();
    }

    public Optional<Korisnik> korisnikPoId(Integer id) {
        return korisnikRepository.findById(id);
    }

    public Korisnik spremiKorisnika(Korisnik korisnik) {
        return korisnikRepository.save(korisnik);
    }

    public void obrisiKorisnika(Integer id) {
        korisnikRepository.deleteById(id);
    }
}
