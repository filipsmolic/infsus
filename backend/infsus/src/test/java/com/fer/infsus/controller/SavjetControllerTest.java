package com.fer.infsus.controller;

import com.fer.infsus.config.TestSecurityConfig;
import com.fer.infsus.service.SavjetService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

@WebMvcTest(SavjetController.class)
@Import(TestSecurityConfig.class)
public class SavjetControllerTest {
    
    @MockBean
    private SavjetService savjetService;
    
    @Test
    void contextLoads() {
    }
}

