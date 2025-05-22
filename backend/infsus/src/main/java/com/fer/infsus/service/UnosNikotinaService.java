package com.fer.infsus.service;

import com.fer.infsus.model.UnosNikotina;
import com.fer.infsus.repository.UnosNikotinaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UnosNikotinaService {
    private final UnosNikotinaRepository unosNikotinaRepository;

    public List<UnosNikotina> sviUnosiNikotina() {
        return unosNikotinaRepository.findAll();
    }

    public Optional<UnosNikotina> unosNikotinaPoId(Integer id) {
        return unosNikotinaRepository.findById(id);
    }

    public UnosNikotina spremiUnosNikotina(UnosNikotina unosNikotina) {
        return unosNikotinaRepository.save(unosNikotina);
    }

    public void obrisiUnosNikotina(Integer id) {
        unosNikotinaRepository.deleteById(id);
    }

    public List<UnosNikotina> unosiZaKorisnikaURasponu(Integer idKorisnik, LocalDateTime od, LocalDateTime doVremena) {
        return unosNikotinaRepository.findByKorisnikIdKorisnikAndDatumBetween(idKorisnik, od, doVremena);
    }
}
