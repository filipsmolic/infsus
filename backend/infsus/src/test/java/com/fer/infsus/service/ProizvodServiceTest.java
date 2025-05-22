package com.fer.infsus.service;

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

import com.fer.infsus.model.Proizvod;
import com.fer.infsus.model.TipProizvoda;
import com.fer.infsus.repository.ProizvodRepository;

@ExtendWith(MockitoExtension.class)
class ProizvodServiceTest {

    @Mock
    private ProizvodRepository proizvodRepository;

    @InjectMocks
    private ProizvodService proizvodService;

    private Proizvod proizvod1;
    private Proizvod proizvod2;
    private TipProizvoda tipProizvoda;

    @BeforeEach
    void setUp() {
        tipProizvoda = new TipProizvoda();
        tipProizvoda.setIdTipProizvoda(1);
        tipProizvoda.setNaziv("Test Tip");

        proizvod1 = new Proizvod();
        proizvod1.setIdProizvod(1);
        proizvod1.setOpis("Test Proizvod 1");
        proizvod1.setNikotinSadrzaj(5.0);
        proizvod1.setTipProizvoda(tipProizvoda);

        proizvod2 = new Proizvod();
        proizvod2.setIdProizvod(2);
        proizvod2.setOpis("Test Proizvod 2");
        proizvod2.setNikotinSadrzaj(10.0);
        proizvod2.setTipProizvoda(tipProizvoda);
    }

    @Test
    void sviProizvodi_ReturnsList() {
        // Arrange
        when(proizvodRepository.findAll()).thenReturn(Arrays.asList(proizvod1, proizvod2));

        // Act
        List<Proizvod> result = proizvodService.sviProizvodi();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(proizvod1, proizvod2);
        verify(proizvodRepository).findAll();
    }

    @Test
    void proizvodPoId_ExistingId_ReturnsProizvod() {
        // Arrange
        when(proizvodRepository.findById(1)).thenReturn(Optional.of(proizvod1));

        // Act
        Optional<Proizvod> result = proizvodService.proizvodPoId(1);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(proizvod1);
        verify(proizvodRepository).findById(1);
    }

    @Test
    void proizvodPoId_NonExistingId_ReturnsEmptyOptional() {
        // Arrange
        when(proizvodRepository.findById(99)).thenReturn(Optional.empty());

        // Act
        Optional<Proizvod> result = proizvodService.proizvodPoId(99);

        // Assert
        assertThat(result).isEmpty();
        verify(proizvodRepository).findById(99);
    }

    @Test
    void spremiProizvod_ValidProizvod_ReturnsSavedProizvod() {
        // Arrange
        when(proizvodRepository.save(any(Proizvod.class))).thenReturn(proizvod1);

        // Act
        Proizvod result = proizvodService.spremiProizvod(proizvod1);

        // Assert
        assertThat(result).isEqualTo(proizvod1);
        verify(proizvodRepository).save(proizvod1);
    }

    @Test
    void obrisiProizvod_ExistingId_DeletesProizvod() {
        // Act
        proizvodService.obrisiProizvod(1);

        // Assert
        verify(proizvodRepository).deleteById(1);
    }
} 