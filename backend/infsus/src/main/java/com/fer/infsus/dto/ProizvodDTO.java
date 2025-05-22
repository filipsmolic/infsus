package com.fer.infsus.dto;

import lombok.Data;

@Data
public class ProizvodDTO {
    private Integer idProizvod;
    private String opis;
    private Double nikotinSadrzaj;
    private Integer idTipProizvoda;
}
