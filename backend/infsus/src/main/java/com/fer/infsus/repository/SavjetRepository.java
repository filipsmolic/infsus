package com.fer.infsus.repository;

import com.fer.infsus.model.Savjet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SavjetRepository extends JpaRepository<Savjet, Integer> {
    List<Savjet> findByKorisnikIdKorisnik(Integer idKorisnik);
}
