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
public class Korisnik {
    @Id
    @Column(name = "IdKorisnik")
    private Integer idKorisnik;

    @Column(name = "Uloga")
    private Integer uloga;

    @Column(name = "Ime")
    private String ime;

    @Column(name = "Email")
    private String email;

    @Column(name = "Lozinka")
    private String lozinka;

    @Column(name = "DatumReg")
    private LocalDateTime datumReg;

    @ManyToOne
    @JoinColumn(name = "IdProizvod")
    private Proizvod proizvod;

}
