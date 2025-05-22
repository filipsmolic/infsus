package com.fer.infsus.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.fer.infsus.model.UnosNikotina;

public interface UnosNikotinaRepository extends JpaRepository<UnosNikotina, Integer> {
    List<UnosNikotina> findByKorisnikIdKorisnikAndDatumBetween(Integer idKorisnik, LocalDateTime od, LocalDateTime doVremena);
    
    Page<UnosNikotina> findByKorisnikIdKorisnikAndDatumBetween(Integer idKorisnik, LocalDateTime od, LocalDateTime doVremena, Pageable pageable);
}
