package com.fer.infsus.dto;

import lombok.Data;

@Data
public class KorisnikDTO {
    private Integer idKorisnik;
    private String ime;
    private String email;
    private Integer uloga;
}
