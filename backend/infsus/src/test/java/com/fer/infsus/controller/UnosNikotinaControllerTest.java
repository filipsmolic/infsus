package com.fer.infsus.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fer.infsus.config.TestSecurityConfig;
import com.fer.infsus.dto.BatchUnosNikotinaDTO;
import com.fer.infsus.dto.UnosNikotinaDTO;
import com.fer.infsus.dto.UnosiZaKorisnikaURasponuDTO;
import com.fer.infsus.mapper.UnosNikotinaMapper;
import com.fer.infsus.model.Korisnik;
import com.fer.infsus.model.Proizvod;
import com.fer.infsus.model.UnosNikotina;
import com.fer.infsus.repository.KorisnikRepository;
import com.fer.infsus.repository.ProizvodRepository;
import com.fer.infsus.service.UnosNikotinaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UnosNikotinaController.class)
@Import(TestSecurityConfig.class)
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

    private UnosNikotina unosNikotina1;
    private UnosNikotina unosNikotina2;
    private UnosNikotinaDTO unosNikotinaDTO1;
    private UnosNikotinaDTO unosNikotinaDTO2;
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

        unosNikotina1 = new UnosNikotina();
        unosNikotina1.setIdUnosNikotina(10);
        unosNikotina1.setKolicina(5);
        unosNikotina1.setDatum(LocalDateTime.of(2025, 5, 1, 12, 0));
        unosNikotina1.setKorisnik(korisnik);
        unosNikotina1.setProizvod(proizvod);

        unosNikotina2 = new UnosNikotina();
        unosNikotina2.setIdUnosNikotina(11);
        unosNikotina2.setKolicina(7);
        unosNikotina2.setDatum(LocalDateTime.of(2025, 5, 2, 12, 0));
        unosNikotina2.setKorisnik(korisnik);
        unosNikotina2.setProizvod(proizvod);

        unosNikotinaDTO1 = new UnosNikotinaDTO();
        unosNikotinaDTO1.setIdUnosNikotina(10);
        unosNikotinaDTO1.setKolicina(5);
        unosNikotinaDTO1.setIdKorisnik(1);
        unosNikotinaDTO1.setIdProizvod(2);
        unosNikotinaDTO1.setDatum(LocalDateTime.of(2025, 5, 1, 12, 0));

        unosNikotinaDTO2 = new UnosNikotinaDTO();
        unosNikotinaDTO2.setIdUnosNikotina(11);
        unosNikotinaDTO2.setKolicina(7);
        unosNikotinaDTO2.setIdKorisnik(1);
        unosNikotinaDTO2.setIdProizvod(2);
        unosNikotinaDTO2.setDatum(LocalDateTime.of(2025, 5, 2, 12, 0));

        unosiZaKorisnikaURasponuDTO = new UnosiZaKorisnikaURasponuDTO();
        unosiZaKorisnikaURasponuDTO.setIdUnosNikotina(10);
        unosiZaKorisnikaURasponuDTO.setKolicina(5);
        unosiZaKorisnikaURasponuDTO.setIdKorisnik(1);
        unosiZaKorisnikaURasponuDTO.setIdProizvod(2);
        unosiZaKorisnikaURasponuDTO.setDatum(LocalDateTime.of(2025, 5, 1, 12, 0));
        unosiZaKorisnikaURasponuDTO.setOpisProizvoda("Test Proizvod");

        when(korisnikRepository.findById(1)).thenReturn(Optional.of(korisnik));
        when(proizvodRepository.findById(2)).thenReturn(Optional.of(proizvod));
        
        // Set up mapper for entity-to-dto conversion
        when(unosNikotinaMapper.toDTO(unosNikotina1)).thenReturn(unosNikotinaDTO1);
        when(unosNikotinaMapper.toDTO(unosNikotina2)).thenReturn(unosNikotinaDTO2);
        when(unosNikotinaMapper.toUnosiZaKorisnikaURasponuDTO(unosNikotina1)).thenReturn(unosiZaKorisnikaURasponuDTO);
    }

    @Test
    void sviUnosiNikotina_ReturnsAllUnosNikotinaDTOs() throws Exception {
        // Arrange
        when(unosNikotinaService.sviUnosiNikotina()).thenReturn(Arrays.asList(unosNikotina1, unosNikotina2));

        // Act & Assert
        mockMvc.perform(get("/api/unosi-nikotina"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].idUnosNikotina", is(10)))
                .andExpect(jsonPath("$[0].kolicina", is(5)))
                .andExpect(jsonPath("$[1].idUnosNikotina", is(11)))
                .andExpect(jsonPath("$[1].kolicina", is(7)));

        verify(unosNikotinaService).sviUnosiNikotina();
        verify(unosNikotinaMapper).toDTO(unosNikotina1);
        verify(unosNikotinaMapper).toDTO(unosNikotina2);
    }

    @Test
    void unosNikotinaPoId_ExistingId_ReturnsUnosNikotinaDTO() throws Exception {
        // Arrange
        when(unosNikotinaService.unosNikotinaPoId(10)).thenReturn(Optional.of(unosNikotina1));

        // Act & Assert
        mockMvc.perform(get("/api/unosi-nikotina/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUnosNikotina", is(10)))
                .andExpect(jsonPath("$.kolicina", is(5)))
                .andExpect(jsonPath("$.datum").exists());

        verify(unosNikotinaService).unosNikotinaPoId(10);
        verify(unosNikotinaMapper).toDTO(unosNikotina1);
    }

    @Test
    void dodajUnosNikotina_ValidDTO_ReturnsSavedDTO() throws Exception {
        // Arrange
        LocalDateTime testDatum = LocalDateTime.of(2025, 6, 15, 14, 30);
        
        UnosNikotinaDTO inputDto = new UnosNikotinaDTO();
        inputDto.setKolicina(15);
        inputDto.setIdKorisnik(1);
        inputDto.setIdProizvod(2);
        inputDto.setDatum(testDatum);

        UnosNikotina entityToSave = new UnosNikotina();
        entityToSave.setKolicina(15);
        entityToSave.setKorisnik(korisnik);
        entityToSave.setProizvod(proizvod);
        entityToSave.setDatum(testDatum);

        UnosNikotina savedEntity = new UnosNikotina();
        savedEntity.setIdUnosNikotina(20);
        savedEntity.setKolicina(15);
        savedEntity.setKorisnik(korisnik);
        savedEntity.setProizvod(proizvod);
        savedEntity.setDatum(testDatum);

        UnosNikotinaDTO savedDto = new UnosNikotinaDTO();
        savedDto.setIdUnosNikotina(20);
        savedDto.setKolicina(15);
        savedDto.setIdKorisnik(1);
        savedDto.setIdProizvod(2);
        savedDto.setDatum(testDatum);

        when(unosNikotinaMapper.fromDTO(inputDto)).thenReturn(entityToSave);
        when(unosNikotinaService.spremiUnosNikotina(entityToSave)).thenReturn(savedEntity);
        when(unosNikotinaMapper.toDTO(savedEntity)).thenReturn(savedDto);

        // Act & Assert
        mockMvc.perform(post("/api/unosi-nikotina")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUnosNikotina", is(20)))
                .andExpect(jsonPath("$.kolicina", is(15)))
                .andExpect(jsonPath("$.datum").exists());

        verify(unosNikotinaMapper).fromDTO(any(UnosNikotinaDTO.class));
        verify(unosNikotinaService).spremiUnosNikotina(any(UnosNikotina.class));
        verify(unosNikotinaMapper).toDTO(any(UnosNikotina.class));
    }

    @Test
    void azurirajUnosNikotina_ValidDTO_ReturnsUpdatedDTO() throws Exception {
        // Arrange
        LocalDateTime testDatum = LocalDateTime.of(2025, 6, 15, 14, 30);
        
        UnosNikotinaDTO inputDto = new UnosNikotinaDTO();
        inputDto.setKolicina(25);
        inputDto.setIdKorisnik(1);
        inputDto.setIdProizvod(2);
        inputDto.setDatum(testDatum);

        UnosNikotina entityToUpdate = new UnosNikotina();
        entityToUpdate.setKolicina(25);
        entityToUpdate.setKorisnik(korisnik);
        entityToUpdate.setProizvod(proizvod);
        entityToUpdate.setDatum(testDatum);

        UnosNikotina updatedEntity = new UnosNikotina();
        updatedEntity.setIdUnosNikotina(10);
        updatedEntity.setKolicina(25);
        updatedEntity.setKorisnik(korisnik);
        updatedEntity.setProizvod(proizvod);
        updatedEntity.setDatum(testDatum);

        UnosNikotinaDTO updatedDto = new UnosNikotinaDTO();
        updatedDto.setIdUnosNikotina(10);
        updatedDto.setKolicina(25);
        updatedDto.setIdKorisnik(1);
        updatedDto.setIdProizvod(2);
        updatedDto.setDatum(testDatum);

        when(unosNikotinaMapper.fromDTO(inputDto)).thenReturn(entityToUpdate);
        when(unosNikotinaService.spremiUnosNikotina(any(UnosNikotina.class))).thenReturn(updatedEntity);
        when(unosNikotinaMapper.toDTO(updatedEntity)).thenReturn(updatedDto);

        // Act & Assert
        mockMvc.perform(put("/api/unosi-nikotina/10")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUnosNikotina", is(10)))
                .andExpect(jsonPath("$.kolicina", is(25)))
                .andExpect(jsonPath("$.datum").exists());

        verify(unosNikotinaMapper).fromDTO(any(UnosNikotinaDTO.class));
        verify(unosNikotinaService).spremiUnosNikotina(any(UnosNikotina.class));
        verify(unosNikotinaMapper).toDTO(any(UnosNikotina.class));
    }

    @Test
    void obrisiUnosNikotina_ExistingId_NoContent() throws Exception {
        // Arrange
        doNothing().when(unosNikotinaService).obrisiUnosNikotina(10);

        // Act & Assert
        mockMvc.perform(delete("/api/unosi-nikotina/10"))
                .andExpect(status().isOk());

        verify(unosNikotinaService).obrisiUnosNikotina(10);
    }

    @Test
    void unosiZaKorisnikaURasponu_ValidParams_ReturnsFilteredDTOs() throws Exception {
        // Arrange
        LocalDateTime od = LocalDateTime.of(2025, 5, 1, 0, 0, 0);
        LocalDateTime doVreme = LocalDateTime.of(2025, 5, 5, 0, 0, 0);
        
        Page<UnosNikotina> page = new PageImpl<>(List.of(unosNikotina1));
        
        when(unosNikotinaService.unosiZaKorisnikaURasponu(
                eq(1), 
                any(LocalDateTime.class), 
                any(LocalDateTime.class),
                any(Pageable.class)))
                .thenReturn(page);

        // Act & Assert
        mockMvc.perform(get("/api/unosi-nikotina/korisnik/1")
                .param("od", "2025-05-01T00:00:00")
                .param("do", "2025-05-05T00:00:00")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].idUnosNikotina", is(10)))
                .andExpect(jsonPath("$.content[0].kolicina", is(5)));

        verify(unosNikotinaService).unosiZaKorisnikaURasponu(
                eq(1), 
                any(LocalDateTime.class), 
                any(LocalDateTime.class),
                any(Pageable.class));
        verify(unosNikotinaMapper).toUnosiZaKorisnikaURasponuDTO(unosNikotina1);
    }

    @Test
    void batchUnosNikotina_ValidBatchDTO_ReturnsSavedDTOs() throws Exception {
        // Arrange
        BatchUnosNikotinaDTO.ProizvodUnosDTO proizvodDTO = new BatchUnosNikotinaDTO.ProizvodUnosDTO();
        proizvodDTO.setIdProizvod(2);
        proizvodDTO.setKolicina(5);

        BatchUnosNikotinaDTO batchDTO = new BatchUnosNikotinaDTO();
        batchDTO.setIdKorisnik(1);
        batchDTO.setDatum(LocalDate.of(2025, 5, 1));
        batchDTO.setProizvodi(List.of(proizvodDTO));

        UnosNikotina savedEntity = new UnosNikotina();
        savedEntity.setIdUnosNikotina(10);
        savedEntity.setKolicina(5);
        savedEntity.setDatum(LocalDateTime.of(2025, 5, 1, 0, 0));
        savedEntity.setKorisnik(korisnik);
        savedEntity.setProizvod(proizvod);
        
        UnosNikotinaDTO savedEntityDTO = new UnosNikotinaDTO();
        savedEntityDTO.setIdUnosNikotina(10);
        savedEntityDTO.setKolicina(5);
        savedEntityDTO.setIdKorisnik(1);
        savedEntityDTO.setIdProizvod(2);

        // Mock service to return our saved entity
        when(unosNikotinaService.spremiUnosNikotina(any(UnosNikotina.class))).thenReturn(savedEntity);
        
        // Mock mapper to return our DTO for the saved entity
        when(unosNikotinaMapper.toDTO(savedEntity)).thenReturn(savedEntityDTO);

        // Act & Assert
        mockMvc.perform(post("/api/unosi-nikotina/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(batchDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].idUnosNikotina", is(10)))
                .andExpect(jsonPath("$[0].kolicina", is(5)));

        verify(korisnikRepository).findById(1);
        verify(proizvodRepository).findById(2);
        verify(unosNikotinaService).spremiUnosNikotina(any(UnosNikotina.class));
        verify(unosNikotinaMapper).toDTO(any(UnosNikotina.class));
    }
}
