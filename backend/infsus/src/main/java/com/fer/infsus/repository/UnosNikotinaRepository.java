package com.fer.infsus.repository;

import com.fer.infsus.model.UnosNikotina;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface UnosNikotinaRepository extends JpaRepository<UnosNikotina, Integer> {
    List<UnosNikotina> findByKorisnikIdKorisnikAndDatumBetween(Integer idKorisnik, LocalDateTime od, LocalDateTime doVremena);
}
