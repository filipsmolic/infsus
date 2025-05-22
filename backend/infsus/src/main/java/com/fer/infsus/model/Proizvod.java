package com.fer.infsus.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Proizvod {
    @Id
    @Column(name = "IdProizvod")
    private Integer idProizvod;

    @Column(name = "Opis")
    private String opis;

    @Column(name = "NikotinSadrzaj")
    private Double nikotinSadrzaj;

    @ManyToOne
    @JoinColumn(name = "IdTipProizvoda")
    private TipProizvoda tipProizvoda;

}
