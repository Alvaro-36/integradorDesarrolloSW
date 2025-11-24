package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.DnaRequestDTO;
import org.example.service.MutantService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MutantController.class)
class MutantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MutantService mutantService;

    // ========== TESTS POST /mutant/ ==========

    @Test
    @DisplayName("POST /mutant/ - Retorna 200 cuando es mutante")
    void testCheckMutant_ReturnOk_WhenIsMutant() throws Exception {
        String[] dna = {
            "ATGCGA",
            "CAGTGC",
            "TTATGT",
            "AGAAGG",
            "CCCCTA",
            "TCACTG"
        };
        
        DnaRequestDTO request = DnaRequestDTO.builder()
            .dna(dna)
            .build();

        when(mutantService.analyzeDna(any(String[].class))).thenReturn(true);

        mockMvc.perform(post("/mutant/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /mutant/ - Retorna 403 cuando NO es mutante")
    void testCheckMutant_ReturnForbidden_WhenIsNotMutant() throws Exception {
        String[] dna = {
            "ATGCGA",
            "CAGTGC",
            "TTATGT",
            "AGACGG",
            "GCGTCA",
            "TCACTG"
        };
        
        DnaRequestDTO request = DnaRequestDTO.builder()
            .dna(dna)
            .build();

        when(mutantService.analyzeDna(any(String[].class))).thenReturn(false);

        mockMvc.perform(post("/mutant/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("POST /mutant/ - Retorna 400 cuando el ADN es nulo")
    void testCheckMutant_ReturnBadRequest_WhenDnaIsNull() throws Exception {
        DnaRequestDTO request = DnaRequestDTO.builder()
            .dna(null)
            .build();

        mockMvc.perform(post("/mutant/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /mutant/ - Retorna 400 cuando el ADN está vacío")
    void testCheckMutant_ReturnBadRequest_WhenDnaIsEmpty() throws Exception {
        DnaRequestDTO request = DnaRequestDTO.builder()
            .dna(new String[]{})
            .build();

        mockMvc.perform(post("/mutant/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /mutant/ - Retorna 400 cuando la matriz no es cuadrada")
    void testCheckMutant_ReturnBadRequest_WhenMatrixIsNotSquare() throws Exception {
        String[] dna = {
            "ATGCGA",
            "CAGTGC",
            "TTAT"  // Fila más corta
        };
        
        DnaRequestDTO request = DnaRequestDTO.builder()
            .dna(dna)
            .build();

        mockMvc.perform(post("/mutant/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /mutant/ - Retorna 400 cuando contiene caracteres inválidos")
    void testCheckMutant_ReturnBadRequest_WhenContainsInvalidCharacters() throws Exception {
        String[] dna = {
            "ATGCGA",
            "CAGTGC",
            "TTATGT",
            "AGAXGG",  // X es inválido
            "CCCCTA",
            "TCACTG"
        };
        
        DnaRequestDTO request = DnaRequestDTO.builder()
            .dna(dna)
            .build();

        mockMvc.perform(post("/mutant/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    // ========== TESTS GET /mutant/stats ==========

    @Test
    @DisplayName("GET /mutant/stats - Retorna 200 con estadísticas correctas")
    void testGetStats_ReturnOk_WithCorrectStats() throws Exception {
        when(mutantService.countAllMutants()).thenReturn(40L);
        when(mutantService.countAllHumans()).thenReturn(100L);
        when(mutantService.findAllMutants()).thenReturn(Collections.nCopies(40, null));
        when(mutantService.findAllHumans()).thenReturn(Collections.nCopies(100, null));

        mockMvc.perform(get("/mutant/stats")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cantMutantes").value(40))
                .andExpect(jsonPath("$.cantHumanos").value(100))
                .andExpect(jsonPath("$.ratio").value(0.4));
    }

    @Test
    @DisplayName("GET /mutant/stats - Retorna 200 cuando no hay registros")
    void testGetStats_ReturnOk_WhenNoRecords() throws Exception {
        when(mutantService.countAllMutants()).thenReturn(0L);
        when(mutantService.countAllHumans()).thenReturn(0L);
        when(mutantService.findAllMutants()).thenReturn(Collections.emptyList());
        when(mutantService.findAllHumans()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/mutant/stats")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cantMutantes").value(0))
                .andExpect(jsonPath("$.cantHumanos").value(0));
    }

    @Test
    @DisplayName("GET /mutant/stats - Retorna 200 con solo mutantes")
    void testGetStats_ReturnOk_WithOnlyMutants() throws Exception {
        when(mutantService.countAllMutants()).thenReturn(10L);
        when(mutantService.countAllHumans()).thenReturn(0L);
        when(mutantService.findAllMutants()).thenReturn(Collections.nCopies(10, null));
        when(mutantService.findAllHumans()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/mutant/stats")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cantMutantes").value(10))
                .andExpect(jsonPath("$.cantHumanos").value(0));
    }
}
