package com.fer.infsus.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import com.fer.infsus.service.TipProizvodaService;

@WebMvcTest(TipProizvodaController.class)
public class TipProizvodaControllerTest {

    @Mock
    private TipProizvodaService tipProizvodaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void contextLoads() {
    }
}

