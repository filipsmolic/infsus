package com.fer.infsus.repository;

import com.fer.infsus.model.UnosKupnje;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface UnosKupnjeRepository extends JpaRepository<UnosKupnje, Integer> {
    List<UnosKupnje> findByKorisnikIdKorisnikAndDatumBetween(Integer idKorisnik, LocalDateTime od, LocalDateTime doVremena);
}
