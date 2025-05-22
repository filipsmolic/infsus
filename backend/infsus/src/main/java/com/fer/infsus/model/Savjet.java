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
public class Savjet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdSavjet")
    private Integer idSavjet;

    @Column(name = "Izvor")
    private String izvor;

    @Column(name = "Tekst")
    private String tekst;

    @Column(name = "Datum")
    private LocalDateTime datum;

    @ManyToOne
    @JoinColumn(name = "IdKorisnik")
    private Korisnik korisnik;

}
