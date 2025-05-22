package com.fer.infsus.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fer.infsus.dto.BatchUnosNikotinaDTO;
import com.fer.infsus.dto.UnosNikotinaDTO;
import com.fer.infsus.dto.UnosiZaKorisnikaURasponuDTO;
import com.fer.infsus.mapper.UnosNikotinaMapper;
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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
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

    @MockBean
    private UnosNikotinaMapper unosNikotinaMapper;

    private UnosNikotina u1;
    private UnosNikotina u2;
    private UnosNikotinaDTO u1DTO;
    private UnosNikotinaDTO u2DTO;
    private UnosiZaKorisnikaURasponuDTO unosiZaKorisnikaURasponuDTO;
    private Korisnik korisnik;
    private Proizvod proizvod;

    @BeforeEach
    void setup() {
        korisnik = new Korisnik();
        korisnik.setIdKorisnik(1);
        korisnik.setIme("Test User");
        korisnik.setEmail("test@example.com");

        proizvod = new Proizvod();
        proizvod.setIdProizvod(2);
        proizvod.setOpis("Test Proizvod");
        proizvod.setNikotinSadrzaj(5.0);

        u1 = new UnosNikotina();
        u1.setIdUnosNikotina(10);
        u1.setKolicina(5);
        u1.setDatum(LocalDateTime.of(2025,5,1,12,0));
        u1.setKorisnik(korisnik);
        u1.setProizvod(proizvod);

        u2 = new UnosNikotina();
        u2.setIdUnosNikotina(11);
        u2.setKolicina(7);
        u2.setDatum(LocalDateTime.of(2025,5,2,12,0));
        u2.setKorisnik(korisnik);
        u2.setProizvod(proizvod);

        u1DTO = new UnosNikotinaDTO();
        u1DTO.setIdUnosNikotina(10);
        u1DTO.setKolicina(5);
        u1DTO.setIdKorisnik(1);
        u1DTO.setIdProizvod(2);

        u2DTO = new UnosNikotinaDTO();
        u2DTO.setIdUnosNikotina(11);
        u2DTO.setKolicina(7);
        u2DTO.setIdKorisnik(1);
        u2DTO.setIdProizvod(2);

        unosiZaKorisnikaURasponuDTO = new UnosiZaKorisnikaURasponuDTO();
        unosiZaKorisnikaURasponuDTO.setIdUnosNikotina(10);
        unosiZaKorisnikaURasponuDTO.setKolicina(5);
        unosiZaKorisnikaURasponuDTO.setIdKorisnik(1);
        unosiZaKorisnikaURasponuDTO.setIdProizvod(2);
        unosiZaKorisnikaURasponuDTO.setDatum(LocalDateTime.of(2025,5,1,12,0));
        unosiZaKorisnikaURasponuDTO.setOpisProizvoda("Test Proizvod");

        given(korisnikRepository.findById(1)).willReturn(Optional.of(korisnik));
        given(proizvodRepository.findById(2)).willReturn(Optional.of(proizvod));
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
        BatchUnosNikotinaDTO.ProizvodUnosDTO proizvodDTO = new BatchUnosNikotinaDTO.ProizvodUnosDTO();
        proizvodDTO.setIdProizvod(2);
        proizvodDTO.setKolicina(5);

        BatchUnosNikotinaDTO batchDTO = new BatchUnosNikotinaDTO();
        batchDTO.setIdKorisnik(1);
        batchDTO.setDatum(LocalDate.of(2025, 5, 1));
        batchDTO.setProizvodi(List.of(proizvodDTO));

        UnosNikotina u1 = new UnosNikotina();
        u1.setIdUnosNikotina(10);
        u1.setKolicina(5);
        UnosNikotina u2 = new UnosNikotina();
        u2.setIdUnosNikotina(11);
        u2.setKolicina(10);

        given(unosNikotinaService.spremiUnosNikotina(any())).willReturn(u1, u2);

        mockMvc.perform(post("/api/unosi-nikotina/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(batchDTO)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].idUnosNikotina").value(10))
               .andExpect(jsonPath("$[1].idUnosNikotina").value(11));
    }

    @Test
    void batchUnosNikotina_ValidBatchDTO_ReturnsListOfSavedDTOs() throws Exception {
        // Arrange
        BatchUnosNikotinaDTO.ProizvodUnosDTO proizvodDTO = new BatchUnosNikotinaDTO.ProizvodUnosDTO();
        proizvodDTO.setIdProizvod(1);
        proizvodDTO.setKolicina(2);
        
        BatchUnosNikotinaDTO batchDTO = new BatchUnosNikotinaDTO();
        batchDTO.setIdKorisnik(1);
        batchDTO.setDatum(LocalDate.now());
        batchDTO.setProizvodi(List.of(proizvodDTO));
        
        when(korisnikRepository.findById(1)).thenReturn(Optional.of(korisnik));
        when(proizvodRepository.findById(1)).thenReturn(Optional.of(proizvod));
        when(unosNikotinaService.spremiUnosNikotina(any(UnosNikotina.class))).thenReturn(u1);
        when(unosNikotinaMapper.toDTO(any(UnosNikotina.class))).thenReturn(u1DTO);

        // Act & Assert
        mockMvc.perform(post("/api/unosi-nikotina/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(batchDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].idUnosNikotina", is(10)))
                .andExpect(jsonPath("$[0].kolicina", is(5)));
        
        verify(korisnikRepository).findById(1);
        verify(proizvodRepository).findById(1);
        verify(unosNikotinaService).spremiUnosNikotina(any(UnosNikotina.class));
    }
}
