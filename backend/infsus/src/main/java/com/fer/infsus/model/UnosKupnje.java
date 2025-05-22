package com.fer.infsus.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UnosKupnje {
    @Id
    @Column(name = "IdKupnja")
    private Integer idKupnja;

    @Column(name = "Datum")
    private LocalDateTime datum;

    @Column(name = "Kolicina")
    private Integer kolicina;

    @Column(name = "Cijena")
    private Double cijena;

    @ManyToOne
    @JoinColumn(name = "IdKorisnik")
    private Korisnik korisnik;

    @ManyToOne
    @JoinColumn(name = "IdProizvod")
    private Proizvod proizvod;

}
