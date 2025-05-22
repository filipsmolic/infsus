package com.fer.infsus.repository;

import com.fer.infsus.model.Savjet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class SavjetRepositoryTest {
    @Autowired
    private SavjetRepository savjetRepository;
    @Test
    void testSaveAndFindById() {
        Savjet savjet = new Savjet();
        savjet.setIzvor("Test izvor");
        savjet.setTekst("Test tekst");
        Savjet saved = savjetRepository.save(savjet);
        assertNotNull(saved.getIdSavjet());
        assertTrue(savjetRepository.findById(saved.getIdSavjet()).isPresent());
    }
}
