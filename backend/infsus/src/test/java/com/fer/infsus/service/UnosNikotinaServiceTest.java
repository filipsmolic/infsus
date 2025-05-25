package com.fer.infsus.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fer.infsus.model.Korisnik;
import com.fer.infsus.model.Proizvod;
import com.fer.infsus.model.UnosNikotina;
import com.fer.infsus.repository.UnosNikotinaRepository;

@ExtendWith(MockitoExtension.class)
class UnosNikotinaServiceTest {

    @Mock
    private UnosNikotinaRepository unosNikotinaRepository;

    @InjectMocks
    private UnosNikotinaService unosNikotinaService;

    private UnosNikotina unosNikotina1;
    private UnosNikotina unosNikotina2;
    private Korisnik korisnik;
    private Proizvod proizvod;

    @BeforeEach
    void setUp() {
        korisnik = new Korisnik();
        korisnik.setIdKorisnik(1);
        korisnik.setIme("Test");
        korisnik.setEmail("test.user@example.com");

        proizvod = new Proizvod();
        proizvod.setIdProizvod(1);
        proizvod.setOpis("Test Proizvod");
        proizvod.setNikotinSadrzaj(5.0);

        unosNikotina1 = new UnosNikotina();
        unosNikotina1.setIdUnosNikotina(1);
        unosNikotina1.setKorisnik(korisnik);
        unosNikotina1.setProizvod(proizvod);
        unosNikotina1.setKolicina(2);
        unosNikotina1.setDatum(LocalDateTime.now().minusDays(1));

        unosNikotina2 = new UnosNikotina();
        unosNikotina2.setIdUnosNikotina(2);
        unosNikotina2.setKorisnik(korisnik);
        unosNikotina2.setProizvod(proizvod);
        unosNikotina2.setKolicina(3);
        unosNikotina2.setDatum(LocalDateTime.now());
    }

    @Test
    void sviUnosiNikotina_ReturnsList() {
         
        when(unosNikotinaRepository.findAll()).thenReturn(Arrays.asList(unosNikotina1, unosNikotina2));

         
        List<UnosNikotina> result = unosNikotinaService.sviUnosiNikotina();

         
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(unosNikotina1, unosNikotina2);
        verify(unosNikotinaRepository).findAll();
    }

    @Test
    void unosNikotinaPoId_ExistingId_ReturnsUnosNikotina() {
         
        when(unosNikotinaRepository.findById(1)).thenReturn(Optional.of(unosNikotina1));

         
        Optional<UnosNikotina> result = unosNikotinaService.unosNikotinaPoId(1);

         
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(unosNikotina1);
        verify(unosNikotinaRepository).findById(1);
    }

    @Test
    void unosNikotinaPoId_NonExistingId_ReturnsEmptyOptional() {
         
        when(unosNikotinaRepository.findById(99)).thenReturn(Optional.empty());

         
        Optional<UnosNikotina> result = unosNikotinaService.unosNikotinaPoId(99);

         
        assertThat(result).isEmpty();
        verify(unosNikotinaRepository).findById(99);
    }

    @Test
    void spremiUnosNikotina_ValidUnosNikotina_ReturnsSavedUnosNikotina() {
         
        when(unosNikotinaRepository.save(any(UnosNikotina.class))).thenReturn(unosNikotina1);

         
        UnosNikotina result = unosNikotinaService.spremiUnosNikotina(unosNikotina1);

         
        assertThat(result).isEqualTo(unosNikotina1);
        verify(unosNikotinaRepository).save(unosNikotina1);
    }

    @Test
    void obrisiUnosNikotina_ExistingId_DeletesUnosNikotina() {
         
        unosNikotinaService.obrisiUnosNikotina(1);

         
        verify(unosNikotinaRepository).deleteById(1);
    }

    @Test
    void unosiZaKorisnikaURasponu_ValidParams_ReturnsFilteredList() {
         
        LocalDateTime from = LocalDateTime.now().minusDays(2);
        LocalDateTime to = LocalDateTime.now().plusDays(1);
        when(unosNikotinaRepository.findByKorisnikIdKorisnikAndDatumBetween(1, from, to))
                .thenReturn(Arrays.asList(unosNikotina1, unosNikotina2));

         
        List<UnosNikotina> result = unosNikotinaService.unosiZaKorisnikaURasponu(1, from, to);

         
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(unosNikotina1, unosNikotina2);
        verify(unosNikotinaRepository).findByKorisnikIdKorisnikAndDatumBetween(1, from, to);
    }
}
