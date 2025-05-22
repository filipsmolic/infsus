package com.fer.infsus.mapper;

import org.springframework.stereotype.Component;

import com.fer.infsus.dto.UnosNikotinaDTO;
import com.fer.infsus.dto.UnosiZaKorisnikaURasponuDTO;
import com.fer.infsus.model.UnosNikotina;
import com.fer.infsus.repository.KorisnikRepository;
import com.fer.infsus.repository.ProizvodRepository;

@Component
public class UnosNikotinaMapper {
    private final KorisnikRepository korisnikRepository;
    private final ProizvodRepository proizvodRepository;

    public UnosNikotinaMapper(KorisnikRepository korisnikRepository, ProizvodRepository proizvodRepository) {
        this.korisnikRepository = korisnikRepository;
        this.proizvodRepository = proizvodRepository;
    }

    public UnosNikotinaDTO toDTO(UnosNikotina u) {
        UnosNikotinaDTO dto = new UnosNikotinaDTO();
        dto.setIdUnosNikotina(u.getIdUnosNikotina());
        dto.setKolicina(u.getKolicina());
        dto.setIdKorisnik(u.getKorisnik() != null ? u.getKorisnik().getIdKorisnik() : null);
        dto.setIdProizvod(u.getProizvod() != null ? u.getProizvod().getIdProizvod() : null);
        dto.setDatum(u.getDatum());
        return dto;
    }

    public UnosNikotina fromDTO(UnosNikotinaDTO dto) {
        UnosNikotina u = new UnosNikotina();
        u.setIdUnosNikotina(dto.getIdUnosNikotina());
        u.setKolicina(dto.getKolicina());
        u.setDatum(dto.getDatum());
        if (dto.getIdKorisnik() != null) {
            u.setKorisnik(korisnikRepository.findById(dto.getIdKorisnik()).orElse(null));
        }
        if (dto.getIdProizvod() != null) {
            u.setProizvod(proizvodRepository.findById(dto.getIdProizvod()).orElse(null));
        }
        return u;
    }

    public UnosiZaKorisnikaURasponuDTO toUnosiZaKorisnikaURasponuDTO(UnosNikotina u) {
        UnosiZaKorisnikaURasponuDTO dto = new UnosiZaKorisnikaURasponuDTO();
        dto.setIdUnosNikotina(u.getIdUnosNikotina());
        dto.setKolicina(u.getKolicina());
        dto.setIdKorisnik(u.getKorisnik() != null ? u.getKorisnik().getIdKorisnik() : null);
        dto.setIdProizvod(u.getProizvod() != null ? u.getProizvod().getIdProizvod() : null);
        dto.setDatum(u.getDatum());
        dto.setOpisProizvoda(u.getProizvod() != null ? u.getProizvod().getOpis() : null);
        dto.setNikotinSadrzaj(u.getProizvod() != null ? u.getProizvod().getNikotinSadrzaj() : null);
        return dto;
    }
} 