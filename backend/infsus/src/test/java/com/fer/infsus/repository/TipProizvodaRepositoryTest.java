package com.fer.infsus.repository;

import com.fer.infsus.model.TipProizvoda;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TipProizvodaRepositoryTest {
    @Autowired
    private TipProizvodaRepository tipProizvodaRepository;
    @Test
    void testSaveAndFindById() {
        TipProizvoda tip = new TipProizvoda();
        tip.setNaziv("Test tip");
        TipProizvoda saved = tipProizvodaRepository.save(tip);
        assertNotNull(saved.getIdTipProizvoda());
        assertTrue(tipProizvodaRepository.findById(saved.getIdTipProizvoda()).isPresent());
    }
}
