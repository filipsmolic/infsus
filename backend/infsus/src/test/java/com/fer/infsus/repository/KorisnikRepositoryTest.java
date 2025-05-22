package com.fer.infsus.repository;

import com.fer.infsus.model.Korisnik;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class KorisnikRepositoryTest {
    @Autowired
    private KorisnikRepository korisnikRepository;
    @Test
    void testSaveAndFindById() {
        Korisnik korisnik = new Korisnik();
        korisnik.setIme("Test Korisnik");
        korisnik.setEmail("test@example.com");
        korisnik.setLozinka("lozinka");
        korisnik.setUloga(0);
        Korisnik saved = korisnikRepository.save(korisnik);
        assertNotNull(saved.getIdKorisnik());
        assertTrue(korisnikRepository.findById(saved.getIdKorisnik()).isPresent());
    }
}
