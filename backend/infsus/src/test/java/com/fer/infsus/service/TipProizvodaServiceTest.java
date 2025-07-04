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

import com.fer.infsus.model.TipProizvoda;
import com.fer.infsus.repository.TipProizvodaRepository;

@ExtendWith(MockitoExtension.class)
class TipProizvodaServiceTest {

    @Mock
    private TipProizvodaRepository tipProizvodaRepository;

    @InjectMocks
    private TipProizvodaService tipProizvodaService;

    private TipProizvoda tip1;
    private TipProizvoda tip2;

    @BeforeEach
    void setUp() {
        tip1 = new TipProizvoda();
        tip1.setIdTipProizvoda(1);
        tip1.setNaziv("Cigareta");

        tip2 = new TipProizvoda();
        tip2.setIdTipProizvoda(2);
        tip2.setNaziv("E-cigareta");
    }

    @Test
    void sviTipovi_ReturnsList() {
         
        when(tipProizvodaRepository.findAll()).thenReturn(Arrays.asList(tip1, tip2));

         
        List<TipProizvoda> result = tipProizvodaService.sviTipovi();

         
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(tip1, tip2);
        verify(tipProizvodaRepository).findAll();
    }

    @Test
    void tipPoId_ExistingId_ReturnsTipProizvoda() {
         
        when(tipProizvodaRepository.findById(1)).thenReturn(Optional.of(tip1));

         
        Optional<TipProizvoda> result = tipProizvodaService.tipPoId(1);

         
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(tip1);
        verify(tipProizvodaRepository).findById(1);
    }

    @Test
    void tipPoId_NonExistingId_ReturnsEmptyOptional() {
         
        when(tipProizvodaRepository.findById(99)).thenReturn(Optional.empty());

         
        Optional<TipProizvoda> result = tipProizvodaService.tipPoId(99);

         
        assertThat(result).isEmpty();
        verify(tipProizvodaRepository).findById(99);
    }

    @Test
    void spremiTip_ValidTipProizvoda_ReturnsSavedTipProizvoda() {
         
        when(tipProizvodaRepository.save(any(TipProizvoda.class))).thenReturn(tip1);

         
        TipProizvoda result = tipProizvodaService.spremiTip(tip1);

         
        assertThat(result).isEqualTo(tip1);
        verify(tipProizvodaRepository).save(tip1);
    }

    @Test
    void obrisiTip_ExistingId_DeletesTipProizvoda() {
         
        tipProizvodaService.obrisiTip(1);

         
        verify(tipProizvodaRepository).deleteById(1);
    }
} 