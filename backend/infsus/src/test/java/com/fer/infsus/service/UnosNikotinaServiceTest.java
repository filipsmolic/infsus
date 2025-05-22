package com.fer.infsus.service;

import com.fer.infsus.model.UnosNikotina;
import com.fer.infsus.repository.UnosNikotinaRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UnosNikotinaServiceTest {
    @Mock
    private UnosNikotinaRepository unosNikotinaRepository;
    @InjectMocks
    private UnosNikotinaService unosNikotinaService;

    @Test
    void testSpremiUnosNikotina() {
        UnosNikotina unos = new UnosNikotina(); unos.setKolicina(2);
        when(unosNikotinaRepository.save(unos)).thenReturn(unos);
        UnosNikotina saved = unosNikotinaService.spremiUnosNikotina(unos);
        assertEquals(2, saved.getKolicina());
    }

    @Test
    void testUnosNikotinaPoId() {
        UnosNikotina unos = new UnosNikotina(); unos.setIdUnosNikotina(1);
        when(unosNikotinaRepository.findById(1)).thenReturn(Optional.of(unos));
        Optional<UnosNikotina> found = unosNikotinaService.unosNikotinaPoId(1);
        assertTrue(found.isPresent());
        assertEquals(1, found.get().getIdUnosNikotina());
    }
}
