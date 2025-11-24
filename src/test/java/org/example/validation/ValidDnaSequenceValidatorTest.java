package org.example.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ValidDnaSequenceValidatorTest {

    private ValidDnaSequenceValidator validator;

    @Mock
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        validator = new ValidDnaSequenceValidator();
    }

    @Test
    @DisplayName("Validar ADN correcto NxN con caracteres válidos")
    void testIsValid_CorrectDna() {
        String[] dna = {
            "ATGCGA",
            "CAGTGC",
            "TTATGT",
            "AGAAGG",
            "CCCCTA",
            "TCACTG"
        };
        assertTrue(validator.isValid(dna, context));
    }

    @Test
    @DisplayName("Validar ADN con matriz no cuadrada (filas != columnas)")
    void testIsValid_NotSquareMatrix() {
        String[] dna = {
            "ATGCGA",
            "CAGTGC",
            "TTATGT",
            "AGAAGG",
            "CCCCTA",
            "TCACT" // Falta un caracter
        };
        assertFalse(validator.isValid(dna, context));
    }

    @Test
    @DisplayName("Validar ADN con matriz no cuadrada (filas != cantidad de filas)")
    void testIsValid_NotSquareMatrix_RowsMismatch() {
        String[] dna = {
            "ATGC",
            "CAGT",
            "TTAT" // 3 filas, 4 columnas
        };
        assertFalse(validator.isValid(dna, context));
    }

    @Test
    @DisplayName("Validar ADN con caracteres inválidos (números)")
    void testIsValid_InvalidCharacters_Numbers() {
        String[] dna = {
            "ATGCGA",
            "CAGTGC",
            "TTATGT",
            "AGAAGG",
            "CCCCTA",
            "TCACT1"
        };
        assertFalse(validator.isValid(dna, context));
    }

    @Test
    @DisplayName("Validar ADN con caracteres inválidos (letras no permitidas)")
    void testIsValid_InvalidCharacters_WrongLetters() {
        String[] dna = {
            "ATGCGA",
            "CAGTGC",
            "TTATGT",
            "AGAAGG",
            "CCCCTA",
            "TCACTX" // X no es válido
        };
        assertFalse(validator.isValid(dna, context));
    }

    @Test
    @DisplayName("Validar ADN con caracteres inválidos (símbolos)")
    void testIsValid_InvalidCharacters_Symbols() {
        String[] dna = {
            "ATGCGA",
            "CAGTGC",
            "TTATGT",
            "AGAAGG",
            "CCCCTA",
            "TCACT-"
        };
        assertFalse(validator.isValid(dna, context));
    }

    @Test
    @DisplayName("Validar ADN con fila nula")
    void testIsValid_NullRow() {
        String[] dna = {
            "ATGCGA",
            "CAGTGC",
            null,
            "AGAAGG",
            "CCCCTA",
            "TCACTG"
        };
        assertFalse(validator.isValid(dna, context));
    }

    @Test
    @DisplayName("Validar ADN vacío (array vacío)")
    void testIsValid_EmptyArray() {
        String[] dna = {};
        assertFalse(validator.isValid(dna, context));
    }

    @Test
    @DisplayName("Validar ADN null (debe retornar true para que @NotNull lo maneje)")
    void testIsValid_NullDna() {
        assertTrue(validator.isValid(null, context));
    }

    @Test
    @DisplayName("Validar ADN con minúsculas (NO se permiten)")
    void testIsValid_Lowercase() {
        String[] dna = {
            "atgcga",
            "cagtgc",
            "ttatgt",
            "agaagg",
            "ccccta",
            "tcactg"
        };
        assertFalse(validator.isValid(dna, context));
    }

    @Test
    @DisplayName("Validar ADN mixto mayúsculas y minúsculas (NO se permiten)")
    void testIsValid_MixedCase() {
        String[] dna = {
            "AtGcGa",
            "CaGtGc",
            "TtAtGt",
            "AgAaGg",
            "CcCcTa",
            "TcAcTg"
        };
        assertFalse(validator.isValid(dna, context));
    }
}
