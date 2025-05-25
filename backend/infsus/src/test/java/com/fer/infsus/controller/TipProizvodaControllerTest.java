package com.fer.infsus.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fer.infsus.config.TestSecurityConfig;
import com.fer.infsus.dto.TipProizvodaDTO;
import com.fer.infsus.mapper.TipProizvodaMapper;
import com.fer.infsus.model.TipProizvoda;
import com.fer.infsus.service.TipProizvodaService;
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

@WebMvcTest(TipProizvodaController.class)
@Import(TestSecurityConfig.class)
public class TipProizvodaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TipProizvodaService tipProizvodaService;

    @MockBean
    private TipProizvodaMapper tipProizvodaMapper;

    private TipProizvoda tipProizvoda1;
    private TipProizvoda tipProizvoda2;
    private TipProizvodaDTO tipProizvodaDTO1;
    private TipProizvodaDTO tipProizvodaDTO2;

    @BeforeEach
    void setUp() {
         
        tipProizvoda1 = new TipProizvoda();
        tipProizvoda1.setIdTipProizvoda(1);
        tipProizvoda1.setNaziv("Cigareta");

        tipProizvoda2 = new TipProizvoda();
        tipProizvoda2.setIdTipProizvoda(2);
        tipProizvoda2.setNaziv("E-cigareta");

        tipProizvodaDTO1 = new TipProizvodaDTO();
        tipProizvodaDTO1.setIdTipProizvoda(1);
        tipProizvodaDTO1.setNaziv("Cigareta");

        tipProizvodaDTO2 = new TipProizvodaDTO();
        tipProizvodaDTO2.setIdTipProizvoda(2);
        tipProizvodaDTO2.setNaziv("E-cigareta");

         
        when(tipProizvodaMapper.toDTO(tipProizvoda1)).thenReturn(tipProizvodaDTO1);
        when(tipProizvodaMapper.toDTO(tipProizvoda2)).thenReturn(tipProizvodaDTO2);
    }

    @Test
    void sviTipovi_ReturnsAllTipovi() throws Exception {
         
        List<TipProizvoda> tipProizvoda = Arrays.asList(tipProizvoda1, tipProizvoda2);
        when(tipProizvodaService.sviTipovi()).thenReturn(tipProizvoda);

         
        mockMvc.perform(get("/api/tipovi-proizvoda"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].idTipProizvoda", is(1)))
                .andExpect(jsonPath("$[0].naziv", is("Cigareta")))
                .andExpect(jsonPath("$[1].idTipProizvoda", is(2)))
                .andExpect(jsonPath("$[1].naziv", is("E-cigareta")));

        verify(tipProizvodaService).sviTipovi();
        verify(tipProizvodaMapper).toDTO(tipProizvoda1);
        verify(tipProizvodaMapper).toDTO(tipProizvoda2);
    }

    @Test
    void tipPoId_ExistingId_ReturnsTip() throws Exception {
         
        when(tipProizvodaService.tipPoId(1)).thenReturn(Optional.of(tipProizvoda1));

         
        mockMvc.perform(get("/api/tipovi-proizvoda/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idTipProizvoda", is(1)))
                .andExpect(jsonPath("$.naziv", is("Cigareta")));

        verify(tipProizvodaService).tipPoId(1);
        verify(tipProizvodaMapper).toDTO(tipProizvoda1);
    }

    @Test
    void tipPoId_NonExistingId_ReturnsNull() throws Exception {
         
        when(tipProizvodaService.tipPoId(99)).thenReturn(Optional.empty());

         
        mockMvc.perform(get("/api/tipovi-proizvoda/99"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(tipProizvodaService).tipPoId(99);
    }

    @Test
    void dodajTip_ValidDTO_ReturnsSavedDTO() throws Exception {
         
        TipProizvodaDTO inputDto = new TipProizvodaDTO();
        inputDto.setNaziv("Novi tip");

        TipProizvoda entityToSave = new TipProizvoda();
        entityToSave.setNaziv("Novi tip");

        TipProizvoda savedEntity = new TipProizvoda();
        savedEntity.setIdTipProizvoda(3);
        savedEntity.setNaziv("Novi tip");

        TipProizvodaDTO savedDto = new TipProizvodaDTO();
        savedDto.setIdTipProizvoda(3);
        savedDto.setNaziv("Novi tip");

        when(tipProizvodaMapper.fromDTO(inputDto)).thenReturn(entityToSave);
        when(tipProizvodaService.spremiTip(entityToSave)).thenReturn(savedEntity);
        when(tipProizvodaMapper.toDTO(savedEntity)).thenReturn(savedDto);

         
        mockMvc.perform(post("/api/tipovi-proizvoda")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idTipProizvoda", is(3)))
                .andExpect(jsonPath("$.naziv", is("Novi tip")));

        verify(tipProizvodaMapper).fromDTO(any(TipProizvodaDTO.class));
        verify(tipProizvodaService).spremiTip(any(TipProizvoda.class));
        verify(tipProizvodaMapper).toDTO(any(TipProizvoda.class));
    }

    @Test
    void azurirajTip_ValidDTO_ReturnsUpdatedDTO() throws Exception {
         
        TipProizvodaDTO inputDto = new TipProizvodaDTO();
        inputDto.setNaziv("Ažurirani tip");

        TipProizvoda entityToUpdate = new TipProizvoda();
        entityToUpdate.setNaziv("Ažurirani tip");

        TipProizvoda updatedEntity = new TipProizvoda();
        updatedEntity.setIdTipProizvoda(1);
        updatedEntity.setNaziv("Ažurirani tip");

        TipProizvodaDTO updatedDto = new TipProizvodaDTO();
        updatedDto.setIdTipProizvoda(1);
        updatedDto.setNaziv("Ažurirani tip");

        when(tipProizvodaMapper.fromDTO(inputDto)).thenReturn(entityToUpdate);
        when(tipProizvodaService.spremiTip(any(TipProizvoda.class))).thenReturn(updatedEntity);
        when(tipProizvodaMapper.toDTO(updatedEntity)).thenReturn(updatedDto);

         
        mockMvc.perform(put("/api/tipovi-proizvoda/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idTipProizvoda", is(1)))
                .andExpect(jsonPath("$.naziv", is("Ažurirani tip")));

        verify(tipProizvodaMapper).fromDTO(any(TipProizvodaDTO.class));
        verify(tipProizvodaService).spremiTip(any(TipProizvoda.class));
        verify(tipProizvodaMapper).toDTO(any(TipProizvoda.class));
    }

    @Test
    void obrisiTip_ExistingId_Success() throws Exception {
         
        doNothing().when(tipProizvodaService).obrisiTip(1);

         
        mockMvc.perform(delete("/api/tipovi-proizvoda/1"))
                .andExpect(status().isOk());

        verify(tipProizvodaService).obrisiTip(1);
    }
}

