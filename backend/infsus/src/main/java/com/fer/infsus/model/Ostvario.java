package com.fer.infsus.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@IdClass(OstvarioId.class)
@Getter
@Setter
@NoArgsConstructor
public class Ostvario {
    @Id
    @Column(name = "IdKorisnik")
    private Integer idKorisnik;
    @Id
    @Column(name = "IdPostignuce")
    private Integer idPostignuce;

    @ManyToOne
    @JoinColumn(name = "idKorisnik", insertable = false, updatable = false)
    private Korisnik korisnik;

    @ManyToOne
    @JoinColumn(name = "idPostignuce", insertable = false, updatable = false)
    private Postignuce postignuce;

}
