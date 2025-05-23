package com.fer.infsus.dto;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UnosKupnjeDTO {
    private Integer idKupnja;
    private Integer kolicina;
    private Double cijena;
    private Integer idKorisnik;
    private Integer idProizvod;
    private LocalDateTime datum;
}
