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
import com.fer.infsus.repository.KorisnikRepository;

@ExtendWith(MockitoExtension.class)
class KorisnikServiceTest {

    @Mock
    private KorisnikRepository korisnikRepository;

    @InjectMocks
    private KorisnikService korisnikService;

    private Korisnik korisnik1;
    private Korisnik korisnik2;
    private Proizvod proizvod;

    @BeforeEach
    void setUp() {
        proizvod = new Proizvod();
        proizvod.setIdProizvod(1);
        proizvod.setOpis("Test Proizvod");
        proizvod.setNikotinSadrzaj(5.0);

        korisnik1 = new Korisnik();
        korisnik1.setIdKorisnik(1);
        korisnik1.setIme("Test User 1");
        korisnik1.setEmail("test1@example.com");
        korisnik1.setLozinka("password1");
        korisnik1.setUloga(1);
        korisnik1.setDatumReg(LocalDateTime.now().minusDays(10));
        korisnik1.setProizvod(proizvod);

        korisnik2 = new Korisnik();
        korisnik2.setIdKorisnik(2);
        korisnik2.setIme("Test User 2");
        korisnik2.setEmail("test2@example.com");
        korisnik2.setLozinka("password2");
        korisnik2.setUloga(2);
        korisnik2.setDatumReg(LocalDateTime.now().minusDays(5));
        korisnik2.setProizvod(proizvod);
    }

    @Test
    void sviKorisnici_ReturnsList() {
        // Arrange
        when(korisnikRepository.findAll()).thenReturn(Arrays.asList(korisnik1, korisnik2));

        // Act
        List<Korisnik> result = korisnikService.sviKorisnici();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(korisnik1, korisnik2);
        verify(korisnikRepository).findAll();
    }

    @Test
    void korisnikPoId_ExistingId_ReturnsKorisnik() {
        // Arrange
        when(korisnikRepository.findById(1)).thenReturn(Optional.of(korisnik1));

        // Act
        Optional<Korisnik> result = korisnikService.korisnikPoId(1);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(korisnik1);
        verify(korisnikRepository).findById(1);
    }

    @Test
    void korisnikPoId_NonExistingId_ReturnsEmptyOptional() {
        // Arrange
        when(korisnikRepository.findById(99)).thenReturn(Optional.empty());

        // Act
        Optional<Korisnik> result = korisnikService.korisnikPoId(99);

        // Assert
        assertThat(result).isEmpty();
        verify(korisnikRepository).findById(99);
    }

    @Test
    void spremiKorisnika_ValidKorisnik_ReturnsSavedKorisnik() {
        // Arrange
        when(korisnikRepository.save(any(Korisnik.class))).thenReturn(korisnik1);

        // Act
        Korisnik result = korisnikService.spremiKorisnika(korisnik1);

        // Assert
        assertThat(result).isEqualTo(korisnik1);
        verify(korisnikRepository).save(korisnik1);
    }

    @Test
    void obrisiKorisnika_ExistingId_DeletesKorisnik() {
        // Act
        korisnikService.obrisiKorisnika(1);

        // Assert
        verify(korisnikRepository).deleteById(1);
    }
} 