package com.fer.infsus.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UnosNikotinaDTO {
    private Integer idUnosNikotina;
    private Integer kolicina;
    private Integer idKorisnik;
    private Integer idProizvod;
    private LocalDateTime datum;
}
