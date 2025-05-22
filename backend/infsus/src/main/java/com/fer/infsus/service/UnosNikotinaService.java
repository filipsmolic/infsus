package com.fer.infsus.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fer.infsus.model.UnosNikotina;
import com.fer.infsus.repository.UnosNikotinaRepository;

import lombok.RequiredArgsConstructor;

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

    /**
     * Find entries for a user within a date range (non-pageable version)
     * @deprecated Use the pageable version instead
     */
    @Deprecated
    public List<UnosNikotina> unosiZaKorisnikaURasponu(Integer idKorisnik, LocalDateTime od, LocalDateTime doVremena) {
        return unosNikotinaRepository.findByKorisnikIdKorisnikAndDatumBetween(idKorisnik, od, doVremena);
    }
    
    /**
     * Find entries for a user within a date range with pagination and sorting
     */
    public Page<UnosNikotina> unosiZaKorisnikaURasponu(Integer idKorisnik, LocalDateTime od, LocalDateTime doVremena, Pageable pageable) {
        return unosNikotinaRepository.findByKorisnikIdKorisnikAndDatumBetween(idKorisnik, od, doVremena, pageable);
    }
}
