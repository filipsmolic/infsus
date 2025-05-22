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
public class UnosNikotina {
    @Id
    @Column(name = "IdUnosNikotina")
    private Integer idUnosNikotina;

    @Column(name = "Kolicina")
    private Integer kolicina;

    @Column(name = "Datum")
    private LocalDateTime datum;

    @ManyToOne
    @JoinColumn(name = "IdKorisnik")
    private Korisnik korisnik;

    @ManyToOne
    @JoinColumn(name = "IdProizvod")
    private Proizvod proizvod;

}
