package com.fer.infsus.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class VremenskoPostignuce {
    @Id
    private Integer idPostignuce;
    private Integer brojDanaUvjet;

    @OneToOne
    @JoinColumn(name = "idPostignuce", referencedColumnName = "idPostignuce", insertable = false, updatable = false)
    private Postignuce postignuce;

}
