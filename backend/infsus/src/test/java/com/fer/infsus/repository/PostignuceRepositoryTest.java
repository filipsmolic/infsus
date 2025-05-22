package com.fer.infsus.repository;

import com.fer.infsus.model.Postignuce;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PostignuceRepositoryTest {
    @Autowired
    private PostignuceRepository postignuceRepository;
    @Test
    void testSaveAndFindById() {
        Postignuce postignuce = new Postignuce();
        postignuce.setNaziv("Test postignuce");
        postignuce.setOpis("Opis");
        Postignuce saved = postignuceRepository.save(postignuce);
        assertNotNull(saved.getIdPostignuce());
        assertTrue(postignuceRepository.findById(saved.getIdPostignuce()).isPresent());
    }
}
