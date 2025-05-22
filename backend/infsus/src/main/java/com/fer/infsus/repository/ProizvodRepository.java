package com.fer.infsus.repository;

import com.fer.infsus.model.Proizvod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProizvodRepository extends JpaRepository<Proizvod, Integer> {
}
