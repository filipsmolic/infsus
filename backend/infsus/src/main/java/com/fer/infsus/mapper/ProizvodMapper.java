package com.fer.infsus.mapper;

import com.fer.infsus.dto.ProizvodDTO;
import com.fer.infsus.model.Proizvod;
import com.fer.infsus.repository.TipProizvodaRepository;
import org.springframework.stereotype.Component;

@Component
public class ProizvodMapper {
    private final TipProizvodaRepository tipProizvodaRepository;

    public ProizvodMapper(TipProizvodaRepository tipProizvodaRepository) {
        this.tipProizvodaRepository = tipProizvodaRepository;
    }

    public ProizvodDTO toDTO(Proizvod p) {
        ProizvodDTO dto = new ProizvodDTO();
        dto.setIdProizvod(p.getIdProizvod());
        dto.setOpis(p.getOpis());
        dto.setNikotinSadrzaj(p.getNikotinSadrzaj());
        dto.setIdTipProizvoda(p.getTipProizvoda() != null ? p.getTipProizvoda().getIdTipProizvoda() : null);
        return dto;
    }

    public Proizvod fromDTO(ProizvodDTO dto) {
        Proizvod p = new Proizvod();
        p.setIdProizvod(dto.getIdProizvod());
        p.setOpis(dto.getOpis());
        p.setNikotinSadrzaj(dto.getNikotinSadrzaj());
        
        if (dto.getIdTipProizvoda() != null) {
            p.setTipProizvoda(tipProizvodaRepository.findById(dto.getIdTipProizvoda()).orElse(null));
        }
        
        return p;
    }
} 