package com.fer.infsus.repository;

import com.fer.infsus.model.UnosNikotina;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UnosNikotinaRepositoryTest {
    @Autowired
    private UnosNikotinaRepository unosNikotinaRepository;

    @Test
    void testSaveAndFindById() {
        UnosNikotina unos = new UnosNikotina();
        unos.setKolicina(2);
        unos.setDatum(LocalDateTime.now());
        UnosNikotina saved = unosNikotinaRepository.save(unos);
        assertNotNull(saved.getIdUnosNikotina());
        assertTrue(unosNikotinaRepository.findById(saved.getIdUnosNikotina()).isPresent());
    }
}
