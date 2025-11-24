package org.example.service;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;



@Service
@Builder
@Data
@Slf4j
public class MutantDetector {
    public boolean isMutant(String[] dna) {
        log.debug("Iniciando análisis de ADN para: {}", (Object) dna);
        int filas = dna.length;

        char[][] dnaMatrix = new char[filas][];

        for (int i = 0; i < filas; i++) {
            dnaMatrix[i] = dna[i].toCharArray();
        }

        int columnas = dnaMatrix[0].length;
        int contadorPatrones = 0;

        int i;
        //Busqueda Horizontal
        i = 0;
        while (i < filas && contadorPatrones < 2) {
            int j = 0;
            while (j < columnas - 3 && contadorPatrones < 2) {
                if (this.areEqual(dnaMatrix[i][j], dnaMatrix[i][j + 1], dnaMatrix[i][j + 2], dnaMatrix[i][j + 3])) {
                    contadorPatrones++;
                    log.debug("Secuencia Horizontal encontrada en: fila {} col {}", i, j);
                }
                j++;
            }
            i++;
        }
        //Busqueda vertical
        i = 0;
        while (i < filas-3 && contadorPatrones < 2) {
            int j = 0;
            while (j < columnas && contadorPatrones < 2) {
                if (this.areEqual(dnaMatrix[i][j], dnaMatrix[i+1][j], dnaMatrix[i+2][j], dnaMatrix[i+3][j])) {
                    contadorPatrones++;
                    log.debug("Secuencia vertical encontrada en: fila {} col {}", i, j);
                }
                j++;
            }
            i++;
        }
        //Busqueda oblicua \
        i = 0;
        while (i < filas-3 && contadorPatrones < 2) {
            int j = 0;
            while (j < columnas-3 && contadorPatrones < 2) {
                if (this.areEqual(dnaMatrix[i][j], dnaMatrix[i+1][j+1], dnaMatrix[i+2][j+2], dnaMatrix[i+3][j+3])) {
                    contadorPatrones++;
                    log.debug("Secuencia Horizontal oblicua en: fila {} col {}", i, j);
                }
            j++;
            }
        i++;
        }
        //Busqueda oblicua /
        i = 0;
        while (i < filas-3 && contadorPatrones < 2) {
            int j = 3;
            while (j < columnas && contadorPatrones < 2) {
                if (this.areEqual(dnaMatrix[i][j], dnaMatrix[i+1][j-1], dnaMatrix[i+2][j-2], dnaMatrix[i+3][j-3])) {
                    contadorPatrones++;
                    log.debug("Secuencia oblicua inversa encontrada en: fila {} col {}", i, j);
                }
                j++;
            }
            i++;
        }


        System.out.println(contadorPatrones);
        return contadorPatrones > 1;// Recuerda devolver true solo si hay MÁS de 1 secuencia
    }
    private boolean areEqual(char a, char b, char c, char d) {
        return a == b && a == c && a == d;
    }
}
