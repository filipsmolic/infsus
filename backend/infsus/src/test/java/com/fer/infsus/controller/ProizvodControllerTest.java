package com.fer.infsus.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fer.infsus.config.TestSecurityConfig;
import com.fer.infsus.dto.ProizvodDTO;
import com.fer.infsus.mapper.ProizvodMapper;
import com.fer.infsus.model.Proizvod;
import com.fer.infsus.model.TipProizvoda;
import com.fer.infsus.service.ProizvodService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProizvodController.class)
@Import(TestSecurityConfig.class)
public class ProizvodControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private ProizvodService proizvodService;
    
    @MockBean
    private ProizvodMapper proizvodMapper;
    
    private Proizvod proizvod1;
    private Proizvod proizvod2;
    private ProizvodDTO proizvodDTO1;
    private ProizvodDTO proizvodDTO2;
    private TipProizvoda tipProizvoda;
    
    @BeforeEach
    void setUp() {
        // Set up test data
        tipProizvoda = new TipProizvoda();
        tipProizvoda.setIdTipProizvoda(1);
        tipProizvoda.setNaziv("Cigareta");
        
        proizvod1 = new Proizvod();
        proizvod1.setIdProizvod(1);
        proizvod1.setOpis("Marlboro");
        proizvod1.setNikotinSadrzaj(5.0);
        proizvod1.setTipProizvoda(tipProizvoda);
        
        proizvod2 = new Proizvod();
        proizvod2.setIdProizvod(2);
        proizvod2.setOpis("Camel");
        proizvod2.setNikotinSadrzaj(4.5);
        proizvod2.setTipProizvoda(tipProizvoda);
        
        proizvodDTO1 = new ProizvodDTO();
        proizvodDTO1.setIdProizvod(1);
        proizvodDTO1.setOpis("Marlboro");
        proizvodDTO1.setNikotinSadrzaj(5.0);
        proizvodDTO1.setIdTipProizvoda(1);
        
        proizvodDTO2 = new ProizvodDTO();
        proizvodDTO2.setIdProizvod(2);
        proizvodDTO2.setOpis("Camel");
        proizvodDTO2.setNikotinSadrzaj(4.5);
        proizvodDTO2.setIdTipProizvoda(1);
        
        // Set up mapper behavior
        when(proizvodMapper.toDTO(proizvod1)).thenReturn(proizvodDTO1);
        when(proizvodMapper.toDTO(proizvod2)).thenReturn(proizvodDTO2);
    }
    
    @Test
    void sviProizvodi_ReturnsAllProizvodi() throws Exception {
        // Arrange
        List<Proizvod> proizvodi = Arrays.asList(proizvod1, proizvod2);
        when(proizvodService.sviProizvodi()).thenReturn(proizvodi);
        
        // Act & Assert
        mockMvc.perform(get("/api/proizvodi"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].idProizvod", is(1)))
                .andExpect(jsonPath("$[0].opis", is("Marlboro")))
                .andExpect(jsonPath("$[0].nikotinSadrzaj", is(5.0)))
                .andExpect(jsonPath("$[1].idProizvod", is(2)))
                .andExpect(jsonPath("$[1].opis", is("Camel")));
        
        verify(proizvodService).sviProizvodi();
        verify(proizvodMapper).toDTO(proizvod1);
        verify(proizvodMapper).toDTO(proizvod2);
    }
    
    @Test
    void proizvodPoId_ExistingId_ReturnsProizvod() throws Exception {
        // Arrange
        when(proizvodService.proizvodPoId(1)).thenReturn(Optional.of(proizvod1));
        
        // Act & Assert
        mockMvc.perform(get("/api/proizvodi/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idProizvod", is(1)))
                .andExpect(jsonPath("$.opis", is("Marlboro")))
                .andExpect(jsonPath("$.nikotinSadrzaj", is(5.0)));
        
        verify(proizvodService).proizvodPoId(1);
        verify(proizvodMapper).toDTO(proizvod1);
    }
    
    @Test
    void proizvodPoId_NonExistingId_ReturnsNull() throws Exception {
        // Arrange
        when(proizvodService.proizvodPoId(99)).thenReturn(Optional.empty());
        
        // Act & Assert
        mockMvc.perform(get("/api/proizvodi/99"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
        
        verify(proizvodService).proizvodPoId(99);
    }
    
    @Test
    void dodajProizvod_ValidDTO_ReturnsSavedDTO() throws Exception {
        // Arrange
        ProizvodDTO inputDto = new ProizvodDTO();
        inputDto.setOpis("Winston");
        inputDto.setNikotinSadrzaj(4.2);
        inputDto.setIdTipProizvoda(1);
        
        Proizvod entityToSave = new Proizvod();
        entityToSave.setOpis("Winston");
        entityToSave.setNikotinSadrzaj(4.2);
        entityToSave.setTipProizvoda(tipProizvoda);
        
        Proizvod savedEntity = new Proizvod();
        savedEntity.setIdProizvod(3);
        savedEntity.setOpis("Winston");
        savedEntity.setNikotinSadrzaj(4.2);
        savedEntity.setTipProizvoda(tipProizvoda);
        
        ProizvodDTO savedDto = new ProizvodDTO();
        savedDto.setIdProizvod(3);
        savedDto.setOpis("Winston");
        savedDto.setNikotinSadrzaj(4.2);
        savedDto.setIdTipProizvoda(1);
        
        when(proizvodMapper.fromDTO(inputDto)).thenReturn(entityToSave);
        when(proizvodService.spremiProizvod(entityToSave)).thenReturn(savedEntity);
        when(proizvodMapper.toDTO(savedEntity)).thenReturn(savedDto);
        
        // Act & Assert
        mockMvc.perform(post("/api/proizvodi")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idProizvod", is(3)))
                .andExpect(jsonPath("$.opis", is("Winston")))
                .andExpect(jsonPath("$.nikotinSadrzaj", is(4.2)));
        
        verify(proizvodMapper).fromDTO(any(ProizvodDTO.class));
        verify(proizvodService).spremiProizvod(any(Proizvod.class));
        verify(proizvodMapper).toDTO(any(Proizvod.class));
    }
    
    @Test
    void azurirajProizvod_ValidDTO_ReturnsUpdatedDTO() throws Exception {
        // Arrange
        ProizvodDTO inputDto = new ProizvodDTO();
        inputDto.setOpis("Marlboro Gold");
        inputDto.setNikotinSadrzaj(4.8);
        inputDto.setIdTipProizvoda(1);
        
        Proizvod entityToUpdate = new Proizvod();
        entityToUpdate.setOpis("Marlboro Gold");
        entityToUpdate.setNikotinSadrzaj(4.8);
        entityToUpdate.setTipProizvoda(tipProizvoda);
        
        Proizvod updatedEntity = new Proizvod();
        updatedEntity.setIdProizvod(1);
        updatedEntity.setOpis("Marlboro Gold");
        updatedEntity.setNikotinSadrzaj(4.8);
        updatedEntity.setTipProizvoda(tipProizvoda);
        
        ProizvodDTO updatedDto = new ProizvodDTO();
        updatedDto.setIdProizvod(1);
        updatedDto.setOpis("Marlboro Gold");
        updatedDto.setNikotinSadrzaj(4.8);
        updatedDto.setIdTipProizvoda(1);
        
        when(proizvodMapper.fromDTO(inputDto)).thenReturn(entityToUpdate);
        when(proizvodService.spremiProizvod(any(Proizvod.class))).thenReturn(updatedEntity);
        when(proizvodMapper.toDTO(updatedEntity)).thenReturn(updatedDto);
        
        // Act & Assert
        mockMvc.perform(put("/api/proizvodi/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idProizvod", is(1)))
                .andExpect(jsonPath("$.opis", is("Marlboro Gold")))
                .andExpect(jsonPath("$.nikotinSadrzaj", is(4.8)));
        
        verify(proizvodMapper).fromDTO(any(ProizvodDTO.class));
        verify(proizvodService).spremiProizvod(any(Proizvod.class));
        verify(proizvodMapper).toDTO(any(Proizvod.class));
    }
    
    @Test
    void obrisiProizvod_ExistingId_Success() throws Exception {
        // Arrange
        doNothing().when(proizvodService).obrisiProizvod(1);
        
        // Act & Assert
        mockMvc.perform(delete("/api/proizvodi/1"))
                .andExpect(status().isOk());
        
        verify(proizvodService).obrisiProizvod(1);
    }
}

