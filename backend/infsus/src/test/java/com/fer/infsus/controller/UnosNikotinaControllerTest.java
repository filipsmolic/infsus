package com.fer.infsus.controller;

import com.fer.infsus.dto.UnosNikotinaDTO;
import com.fer.infsus.model.Korisnik;
import com.fer.infsus.model.Proizvod;
import com.fer.infsus.model.UnosNikotina;
import com.fer.infsus.repository.KorisnikRepository;
import com.fer.infsus.repository.ProizvodRepository;
import com.fer.infsus.service.UnosNikotinaService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Collections;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;

@WebMvcTest(UnosNikotinaController.class)
class UnosNikotinaControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UnosNikotinaService unosNikotinaService;
    @MockBean
    private KorisnikRepository korisnikRepository;
    @MockBean
    private ProizvodRepository proizvodRepository;

    @Test
    void testSviUnosiNikotina() throws Exception {
        UnosNikotina unos = new UnosNikotina();
        unos.setIdUnosNikotina(1);
        unos.setKolicina(2);
        Korisnik korisnik = new Korisnik(); korisnik.setIdKorisnik(1); unos.setKorisnik(korisnik);
        Proizvod proizvod = new Proizvod(); proizvod.setIdProizvod(1); unos.setProizvod(proizvod);
        when(unosNikotinaService.sviUnosiNikotina()).thenReturn(Collections.singletonList(unos));
        mockMvc.perform(get("/api/unosi-nikotina"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idUnosNikotina").value(1));
    }

    @Test
    void testDodajUnosNikotina() throws Exception {
        UnosNikotina unos = new UnosNikotina(); unos.setIdUnosNikotina(1); unos.setKolicina(2);
        when(unosNikotinaService.spremiUnosNikotina(any())).thenReturn(unos);
        mockMvc.perform(post("/api/unosi-nikotina")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"kolicina\":2,\"idKorisnik\":1,\"idProizvod\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUnosNikotina").value(1));
    }
}
