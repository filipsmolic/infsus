package com.fer.infsus.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.junit.jupiter.api.BeforeEach;
import com.fer.infsus.service.KorisnikService;

@WebMvcTest(KorisnikController.class)
public class KorisnikControllerTest {

    @Mock
    private KorisnikService korisnikService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void contextLoads() {
    }
}


