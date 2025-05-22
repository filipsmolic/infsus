package com.fer.infsus.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Postignuce {
    @Id
    @Column(name = "IdPostignuce")
    private Integer idPostignuce;

    @Column(name = "Naziv")
    private String naziv;

    @Column(name = "Opis")
    private String opis;

}
