package com.fer.infsus.dto;

import lombok.Data;

@Data
public class SavjetDTO {
    private Integer idSavjet;
    private String izvor;
    private String tekst;
    private Integer idKorisnik;
}
