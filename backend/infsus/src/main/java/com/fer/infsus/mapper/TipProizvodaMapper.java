package com.fer.infsus.mapper;

import com.fer.infsus.dto.TipProizvodaDTO;
import com.fer.infsus.model.TipProizvoda;
import org.springframework.stereotype.Component;

@Component
public class TipProizvodaMapper {
    
    public TipProizvodaDTO toDTO(TipProizvoda t) {
        TipProizvodaDTO dto = new TipProizvodaDTO();
        dto.setIdTipProizvoda(t.getIdTipProizvoda());
        dto.setNaziv(t.getNaziv());
        return dto;
    }
    
    public TipProizvoda fromDTO(TipProizvodaDTO dto) {
        TipProizvoda t = new TipProizvoda();
        t.setIdTipProizvoda(dto.getIdTipProizvoda());
        t.setNaziv(dto.getNaziv());
        return t;
    }
} 