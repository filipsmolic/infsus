package com.fer.infsus.service;

import com.fer.infsus.model.UnosKupnje;
import com.fer.infsus.repository.UnosKupnjeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UnosKupnjeService {
    private final UnosKupnjeRepository unosKupnjeRepository;

    public List<UnosKupnje> sviUnosiKupnje() {
        return unosKupnjeRepository.findAll();
    }

    public Optional<UnosKupnje> unosKupnjePoId(Integer id) {
        return unosKupnjeRepository.findById(id);
    }

    public UnosKupnje spremiUnosKupnje(UnosKupnje unosKupnje) {
        return unosKupnjeRepository.save(unosKupnje);
    }

    public void obrisiUnosKupnje(Integer id) {
        unosKupnjeRepository.deleteById(id);
    }

    public List<UnosKupnje> unosiKupnjeZaKorisnikaURasponu(Integer idKorisnik, LocalDateTime od, LocalDateTime doVremena) {
        return unosKupnjeRepository.findByKorisnikIdKorisnikAndDatumBetween(idKorisnik, od, doVremena);
    }
}
