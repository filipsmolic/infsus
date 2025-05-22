package com.fer.infsus.repository;

import com.fer.infsus.model.Proizvod;
import com.fer.infsus.model.TipProizvoda;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProizvodRepositoryTest {
    @Autowired
    private ProizvodRepository proizvodRepository;
    @Test
    void testSaveAndFindById() {
        TipProizvoda tip = new TipProizvoda();
        tip.setNaziv("Test tip");
        Proizvod proizvod = new Proizvod();
        proizvod.setOpis("Test opis");
        proizvod.setNikotinSadrzaj(1.5);
        proizvod.setTipProizvoda(tip);
        Proizvod saved = proizvodRepository.save(proizvod);
        assertNotNull(saved.getIdProizvod());
        assertTrue(proizvodRepository.findById(saved.getIdProizvod()).isPresent());
    }
}
