package org.example.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MutantDetectorTest {

    private MutantDetector mutantDetector;

    @BeforeEach
    void setUp() {
        mutantDetector = MutantDetector.builder().build();
    }

    // ========== CASOS MUTANTES ==========

    @Test
    @DisplayName("Mutante con secuencia horizontal en primera y segunda fila")
    void testMutant_HorizontalSequenceFirstRow() {
        String[] dna = {
            "AAAA",
            "CCCC",
            "GCTA",
            "TCAT"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Mutante con secuencia horizontal en última fila")
    void testMutant_HorizontalSequenceLastRow() {
        String[] dna = {
            "ATGCGA",
            "CAGTGC",
            "TTATGT",
            "AGAAGG",
            "CCCCTA",
            "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("No mutante con secuencia vertical en primera columna")
    void testMutant_VerticalSequenceFirstColumn() {
        String[] dna = {
            "ATGC",
            "AGTC",
            "ATAC",
            "AGGG"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Mutante con secuencia vertical en última y primera columna")
    void testMutant_VerticalSequenceLastColumn() {
        String[] dna = {
            "ATGCA",
            "CAGTC",
            "TTATC",
            "TGAAC",
            "TCCCC",
            "TCACT"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Mutante con secuencia diagonal principal (\\)")
    void testMutant_DiagonalPrincipal() {
        String[] dna = {
            "ATGCGA",
            "CAGTGC",
            "TTAGGT",
            "AGAACG",
            "CCCCTA",
            "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Mutante con secuencia diagonal inversa (/)")
    void testMutant_DiagonalInversa() {
        String[] dna = {
            "ATGCGA",
            "CAGTAC",
            "TTAAGT",
            "AGAAGG",
            "ACCCTA",
            "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Mutante con múltiples secuencias (horizontal y vertical)")
    void testMutant_MultipleSequences() {
        String[] dna = {
            "AAAATG",
            "CAGTGC",
            "TTATGT",
            "AGAAGG",
            "CCCCTA",
            "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    // ========== CASOS HUMANOS ==========

    @Test
    @DisplayName("Humano sin secuencias mutantes")
    void testHuman_NoSequences() {
        String[] dna = {
            "ATGC",
            "CAGT",
            "TTAG",
            "AGAC"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Humano con solo una secuencia mutante")
    void testHuman_OnlyOneSequence() {
        String[] dna = {
            "ATGCGA",
            "CAGTGC",
            "TTATGT",
            "AGACGG",
            "GCGTCA",
            "TCACTG"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Humano con una secuencia horizontal solamente")
    void testHuman_OnlyHorizontalSequence() {
        String[] dna = {
            "AAAATG",
            "CAGTGC",
            "TTATGT",
            "AGACGG",
            "GCGTCA",
            "TCACTG"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    // ========== CASOS BORDE ==========

    @Test
    @DisplayName("Matriz mínima 4x4 con mutante")
    void testBorder_MinimumSize4x4Mutant() {
        String[] dna = {
            "AAAA",
            "CCCC",
            "TATA",
            "GTGT"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Matriz mínima 4x4 sin mutante")
    void testBorder_MinimumSize4x4Human() {
        String[] dna = {
            "ATGC",
            "CGTA",
            "TACG",
            "GCAT"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Matriz grande 10x10 con mutante")
    void testBorder_LargeMatrix10x10Mutant() {
        String[] dna = {
            "AAAAAAAAAA",
            "CCCCCCCCCC",
            "ATATATATAT",
            "GTGTGTGTGT",
            "ACACACACAC",
            "TGTGTGTGTG",
            "CGCGCGCGCG",
            "TATATATATAT",
            "GGGGGGGGGG",
            "TTTTTTTTTT"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Matriz grande 10x10 sin mutante")
    void testBorder_LargeMatrix10x10Human() {
        String[] dna = {
            "ATGCATGCAT",
            "CGATCGATCG",
            "TACGTACGTA",
            "GCATGCATGC",
            "ATCGATCGAT",
            "CGTACGTACG",
            "TAGCTAGCTA",
            "GCTAGCTAGC",
            "ATCGATCGAT",
            "CGTAGCTACG"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Matriz 6x6 con exactamente 2 secuencias")
    void testBorder_ExactlyTwoSequences() {
        String[] dna = {
            "ATGCGA",
            "CAGTGC",
            "TTATGT",
            "AGAAGG",
            "CCCCTA",
            "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }
}
