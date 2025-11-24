package org.example.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidDnaSequenceValidator implements ConstraintValidator<ValidDnaSequence, String[]> {

    @Override
    public void initialize(ValidDnaSequence constraintAnnotation) {
    }

    @Override
    public boolean isValid(String[] dna, ConstraintValidatorContext context) {
        // Si es null, dejamos que @NotNull lo maneje (o retornamos true aquí para no duplicar error)
        if (dna == null) {
            return true;
        }

        int n = dna.length;
        if (n == 0) return false;

        for (String row : dna) {
            // 1. Validar que no sea nulo la fila
            if (row == null) return false;

            // 2. Validar Matriz Cuadrada (NxN)
            if (row.length() != n) {
                return false;
            }

            // 3. Validar Caracteres (Solo A, T, C, G) usando Regex
            // Explicación: ^ = inicio, [ATCG]+ = uno o más de estos caracteres, $ = fin
            if (!row.matches("[ATCG]+")) {
                return false;
            }
        }

        return true;
    }
}
