package com.fer.infsus.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fer.infsus.config.TestSecurityConfig;
import com.fer.infsus.dto.KorisnikDTO;
import com.fer.infsus.model.Korisnik;
import com.fer.infsus.model.Proizvod;
import com.fer.infsus.service.KorisnikService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(KorisnikController.class)
@Import(TestSecurityConfig.class)
public class KorisnikControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private KorisnikService korisnikService;

    private Korisnik korisnik1;
    private Korisnik korisnik2;
    private KorisnikDTO korisnikDTO1;
    private KorisnikDTO korisnikDTO2;
    private Proizvod proizvod;

    @BeforeEach
    void setUp() {
        proizvod = new Proizvod();
        proizvod.setIdProizvod(1);
        proizvod.setOpis("Test Proizvod");
        proizvod.setNikotinSadrzaj(5.0);

        korisnik1 = new Korisnik();
        korisnik1.setIdKorisnik(1);
        korisnik1.setIme("Ana Anić");
        korisnik1.setEmail("ana@example.com");
        korisnik1.setLozinka("password1");
        korisnik1.setUloga(1);
        korisnik1.setDatumReg(LocalDateTime.now().minusDays(30));
        korisnik1.setProizvod(proizvod);

        korisnik2 = new Korisnik();
        korisnik2.setIdKorisnik(2);
        korisnik2.setIme("Ivo Ivić");
        korisnik2.setEmail("ivo@example.com");
        korisnik2.setLozinka("password2");
        korisnik2.setUloga(2);
        korisnik2.setDatumReg(LocalDateTime.now().minusDays(15));
        korisnik2.setProizvod(proizvod);

        korisnikDTO1 = new KorisnikDTO();
        korisnikDTO1.setIdKorisnik(1);
        korisnikDTO1.setIme("Ana Anić");
        korisnikDTO1.setEmail("ana@example.com");
        korisnikDTO1.setUloga(1);

        korisnikDTO2 = new KorisnikDTO();
        korisnikDTO2.setIdKorisnik(2);
        korisnikDTO2.setIme("Ivo Ivić");
        korisnikDTO2.setEmail("ivo@example.com");
        korisnikDTO2.setUloga(2);
    }

    @Test
    void sviKorisnici_ReturnsAllKorisnici() throws Exception {
        
        List<Korisnik> korisnici = Arrays.asList(korisnik1, korisnik2);
        when(korisnikService.sviKorisnici()).thenReturn(korisnici);

       
        mockMvc.perform(get("/api/korisnici"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].idKorisnik", is(1)))
                .andExpect(jsonPath("$[0].ime", is("Ana Anić")))
                .andExpect(jsonPath("$[0].email", is("ana@example.com")))
                .andExpect(jsonPath("$[1].idKorisnik", is(2)))
                .andExpect(jsonPath("$[1].ime", is("Ivo Ivić")));

        verify(korisnikService).sviKorisnici();
    }

    @Test
    void korisnikPoId_ExistingId_ReturnsKorisnik() throws Exception {
        
        when(korisnikService.korisnikPoId(1)).thenReturn(Optional.of(korisnik1));

      
        mockMvc.perform(get("/api/korisnici/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idKorisnik", is(1)))
                .andExpect(jsonPath("$.ime", is("Ana Anić")))
                .andExpect(jsonPath("$.email", is("ana@example.com")));

        verify(korisnikService).korisnikPoId(1);
    }

    @Test
    void korisnikPoId_NonExistingId_ReturnsNull() throws Exception {
      
        when(korisnikService.korisnikPoId(99)).thenReturn(Optional.empty());

        
        mockMvc.perform(get("/api/korisnici/99"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(korisnikService).korisnikPoId(99);
    }

    @Test
    void dodajKorisnika_ValidDTO_ReturnsSavedDTO() throws Exception {
      
        KorisnikDTO inputDto = new KorisnikDTO();
        inputDto.setIme("Marko Marković");
        inputDto.setEmail("marko@example.com");
        inputDto.setUloga(1);

        Korisnik savedEntity = new Korisnik();
        savedEntity.setIdKorisnik(3);
        savedEntity.setIme("Marko Marković");
        savedEntity.setEmail("marko@example.com");
        savedEntity.setUloga(1);

        when(korisnikService.spremiKorisnika(any(Korisnik.class))).thenReturn(savedEntity);

       
        mockMvc.perform(post("/api/korisnici")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idKorisnik", is(3)))
                .andExpect(jsonPath("$.ime", is("Marko Marković")))
                .andExpect(jsonPath("$.email", is("marko@example.com")));

        verify(korisnikService).spremiKorisnika(any(Korisnik.class));
    }

    @Test
    void azurirajKorisnika_ValidDTO_ReturnsUpdatedDTO() throws Exception {
       
        KorisnikDTO inputDto = new KorisnikDTO();
        inputDto.setIme("Ana Anić Updated");
        inputDto.setEmail("ana.updated@example.com");
        inputDto.setUloga(1);

        Korisnik updatedEntity = new Korisnik();
        updatedEntity.setIdKorisnik(1);
        updatedEntity.setIme("Ana Anić Updated");
        updatedEntity.setEmail("ana.updated@example.com");
        updatedEntity.setUloga(1);

        when(korisnikService.spremiKorisnika(any(Korisnik.class))).thenReturn(updatedEntity);

   
        mockMvc.perform(put("/api/korisnici/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idKorisnik", is(1)))
                .andExpect(jsonPath("$.ime", is("Ana Anić Updated")))
                .andExpect(jsonPath("$.email", is("ana.updated@example.com")));

        verify(korisnikService).spremiKorisnika(any(Korisnik.class));
    }

    @Test
    void obrisiKorisnika_ExistingId_Success() throws Exception {
      
        doNothing().when(korisnikService).obrisiKorisnika(1);

       
        mockMvc.perform(delete("/api/korisnici/1"))
                .andExpect(status().isOk());

        verify(korisnikService).obrisiKorisnika(1);
    }
}


