package com.fer.infsus.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fer.infsus.dto.BatchUnosNikotinaDTO;
import com.fer.infsus.dto.UnosNikotinaDTO;
import com.fer.infsus.model.Korisnik;
import com.fer.infsus.model.Proizvod;
import com.fer.infsus.model.UnosNikotina;
import com.fer.infsus.service.UnosNikotinaService;
import com.fer.infsus.repository.KorisnikRepository;
import com.fer.infsus.repository.ProizvodRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UnosNikotinaController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UnosNikotinaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UnosNikotinaService unosNikotinaService;

    @MockBean
    private KorisnikRepository korisnikRepository;

    @MockBean
    private ProizvodRepository proizvodRepository;

    private UnosNikotina u1;
    private UnosNikotina u2;

    @BeforeEach
    void setup() {
        Korisnik k = new Korisnik(); k.setIdKorisnik(1);
        Proizvod p1 = new Proizvod(); p1.setIdProizvod(2);
        Proizvod p2 = new Proizvod(); p2.setIdProizvod(3);

        u1 = new UnosNikotina();
        u1.setIdUnosNikotina(10);
        u1.setKolicina(5);
        u1.setDatum(LocalDateTime.of(2025,5,1,12,0));
        u1.setKorisnik(k);
        u1.setProizvod(p1);

        u2 = new UnosNikotina();
        u2.setIdUnosNikotina(11);
        u2.setKolicina(7);
        u2.setDatum(LocalDateTime.of(2025,5,2,12,0));
        u2.setKorisnik(k);
        u2.setProizvod(p2);

        given(korisnikRepository.findById(1)).willReturn(Optional.of(k));
        given(proizvodRepository.findById(2)).willReturn(Optional.of(p1));
        given(proizvodRepository.findById(3)).willReturn(Optional.of(p2));
    }

    @Test
    void testGetAll() throws Exception {
        List<UnosNikotina> lista = Arrays.asList(u1, u2);
        given(unosNikotinaService.sviUnosiNikotina()).willReturn(lista);

        mockMvc.perform(get("/api/unosi-nikotina"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].idUnosNikotina").value(10))
               .andExpect(jsonPath("$[1].kolicina").value(7));
    }

    @Test
    void testGetById() throws Exception {
        given(unosNikotinaService.unosNikotinaPoId(10)).willReturn(Optional.of(u1));

        mockMvc.perform(get("/api/unosi-nikotina/10"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.idUnosNikotina").value(10))
               .andExpect(jsonPath("$.idKorisnik").value(1));
    }

    @Test
    void testCreate() throws Exception {
        UnosNikotinaDTO dto = new UnosNikotinaDTO();
        dto.setKolicina(15);
        dto.setIdKorisnik(1);
        dto.setIdProizvod(2);

        UnosNikotina saved = new UnosNikotina();
        saved.setIdUnosNikotina(20);
        saved.setKolicina(15);
        given(unosNikotinaService.spremiUnosNikotina(any(UnosNikotina.class))).willReturn(saved);

        mockMvc.perform(post("/api/unosi-nikotina")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.idUnosNikotina").value(20))
               .andExpect(jsonPath("$.kolicina").value(15));
    }

    @Test
    void testUpdate() throws Exception {
        UnosNikotinaDTO dto = new UnosNikotinaDTO();
        dto.setKolicina(25);
        dto.setIdKorisnik(1);
        dto.setIdProizvod(2);

        UnosNikotina updated = new UnosNikotina();
        updated.setIdUnosNikotina(10);
        updated.setKolicina(25);
        given(unosNikotinaService.spremiUnosNikotina(any(UnosNikotina.class))).willReturn(updated);

        mockMvc.perform(put("/api/unosi-nikotina/10")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.kolicina").value(25));
    }

    @Test
    void testDelete() throws Exception {
        doNothing().when(unosNikotinaService).obrisiUnosNikotina(10);

        mockMvc.perform(delete("/api/unosi-nikotina/10"))
               .andExpect(status().isOk());
    }

    @Test
    void testGetByKorisnikInRange() throws Exception {
        List<UnosNikotina> lista = Arrays.asList(u1);
        given(unosNikotinaService
            .unosiZaKorisnikaURasponu(eq(1), any(LocalDateTime.class), any(LocalDateTime.class)))
        .willReturn(lista);

        mockMvc.perform(get("/api/unosi-nikotina/korisnik/1")
                .param("od","2025-05-01T00:00:00")
                .param("do","2025-05-05T00:00:00"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].idUnosNikotina").value(10));
    }

    @Test
    void testBatchUnosNikotina() throws Exception {
        BatchUnosNikotinaDTO batchDto = new BatchUnosNikotinaDTO();
        batchDto.setIdKorisnik(1);
        batchDto.setDatum(LocalDate.of(2025, 5, 1));

        BatchUnosNikotinaDTO.ProizvodUnosDTO proizvod1 = new BatchUnosNikotinaDTO.ProizvodUnosDTO();
        proizvod1.setIdProizvod(2);
        proizvod1.setKolicina(5);

        BatchUnosNikotinaDTO.ProizvodUnosDTO proizvod2 = new BatchUnosNikotinaDTO.ProizvodUnosDTO();
        proizvod2.setIdProizvod(3);
        proizvod2.setKolicina(10);

        batchDto.setProizvodi(Arrays.asList(proizvod1, proizvod2));

        UnosNikotina u1 = new UnosNikotina();
        u1.setIdUnosNikotina(10);
        u1.setKolicina(5);
        UnosNikotina u2 = new UnosNikotina();
        u2.setIdUnosNikotina(11);
        u2.setKolicina(10);

        given(unosNikotinaService.spremiUnosNikotina(any())).willReturn(u1, u2);

        mockMvc.perform(post("/api/unosi-nikotina/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(batchDto)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].idUnosNikotina").value(10))
               .andExpect(jsonPath("$[1].idUnosNikotina").value(11));
    }
}
