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

    @Test
    @DisplayName("Mutante con secuencia horizontal en el medio")
    void testMutant_HorizontalMiddle() {
        String[] dna = {
            "ATGCGA",
            "CAGTGC",
            "TTATGT",
            "AAAAGG",
            "CCCCTA",
            "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Mutante con secuencia vertical en el medio")
    void testMutant_VerticalMiddle() {
        String[] dna = {
            "ATGCGA",
            "CAGTGC",
            "TTATGT",
            "TGACGG",
            "TCGTCA",
            "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Mutante 5x5 con secuencia diagonal")
    void testMutant_5x5_Diagonal() {
        String[] dna = {
            "ATGCG",
            "CAGTC",
            "TTATG",
            "AGAAG",
            "CCCCT"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Humano 5x5 sin secuencias")
    void testHuman_5x5() {
        String[] dna = {
            "ATGCG",
            "CAGTC",
            "TTATG",
            "AGAAG",
            "CCTCT"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Mutante con cruce de secuencias")
    void testMutant_CrossedSequences() {
        String[] dnaCross = {
            "ATGCGA",
            "CAATGC",
            "TAATGT",
            "AAAAAG",
            "CAACTA",
            "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dnaCross));
    }

    @Test
    @DisplayName("Mutante con secuencias superpuestas")
    void testMutant_OverlappingSequences() {
        String[] dna = {
            "AAAAAA",
            "CCCCCC",
            "TTTTTT",
            "GGGGGG",
            "AAAAAA",
            "CCCCCC"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Mutante con secuencia diagonal inversa en esquina")
    void testMutant_DiagonalInversaCorner() {
        String[] dna4 = {
            "AAAT",
            "AATA",
            "ATAA",
            "TTTT"
        };
        assertTrue(mutantDetector.isMutant(dna4));
    }

    // ========== CASOS DE ROBUSTEZ (Entradas no validadas) ==========

    @Test
    @DisplayName("Matriz con caracteres inválidos (no debe detectar mutante)")
    void testRobustness_InvalidCharacters() {
        String[] dna = {
            "TTGCGA", // Cambiado A por T para romper diagonal
            "CAGTGC",
            "TTATGT",
            "AGAATG", // Cambiado G por T para romper vertical
            "CCCCTA", // Secuencia horizontal CCCC (1)
            "TCACTX" // X no es válido
        };
        // Solo hay 1 secuencia (CCCC), por lo que debe retornar false
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Matriz con números (no debe detectar mutante)")
    void testRobustness_Numbers() {
        String[] dna = {
            "1234",
            "5678",
            "9012",
            "3456"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Matriz nula (debe lanzar excepción)")
    void testRobustness_NullDna() {
        assertThrows(NullPointerException.class, () -> mutantDetector.isMutant(null));
    }

    @Test
    @DisplayName("Matriz vacía (debe lanzar excepción o retornar false)")
    void testRobustness_EmptyDna() {
        String[] dna = {};
        // Dependiendo de la implementación, puede lanzar IndexOutOfBounds o retornar false
        // En la implementación actual: dna.length es 0. dnaMatrix[0] lanzará IndexOutOfBounds si intenta acceder
        // Pero el loop i < filas (0) no se ejecuta.
        // Luego int columnas = dnaMatrix[0].length; -> Aquí lanzará IndexOutOfBoundsException porque dnaMatrix[0] no existe
        assertThrows(IndexOutOfBoundsException.class, () -> mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Matriz irregular (Jagged Array) - Fila más corta")
    void testRobustness_JaggedArray_ShortRow() {
        String[] dna = {
            "ATGCGA",
            "CAGTGC",
            "TTAT",   // Fila corta
            "AGAAGG",
            "CCCCTA",
            "TCACTG"
        };
        // Al convertir a char[][], la fila 2 tendrá longitud 4.
        // El algoritmo usa columnas = dnaMatrix[0].length (6).
        // Cuando acceda a dnaMatrix[2][4] o [5], lanzará IndexOutOfBoundsException
        assertThrows(IndexOutOfBoundsException.class, () -> mutantDetector.isMutant(dna));
    }
}
