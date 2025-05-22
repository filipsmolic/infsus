package com.fer.infsus.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class BatchUnosNikotinaDTO {
    private Integer idKorisnik;
    private LocalDate datum;
    private List<ProizvodUnosDTO> proizvodi;

    @Data
    public static class ProizvodUnosDTO {
        private Integer idProizvod;
        private Integer kolicina;
    }
}
