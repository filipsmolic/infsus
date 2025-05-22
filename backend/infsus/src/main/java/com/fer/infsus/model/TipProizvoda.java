package com.fer.infsus.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class TipProizvoda {
    @Id
    @Column(name = "IdTipProizvoda")
    private Integer idTipProizvoda;

    @Column(name = "Naziv")
    private String naziv;

}
