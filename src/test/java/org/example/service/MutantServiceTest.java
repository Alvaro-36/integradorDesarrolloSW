package org.example.service;

import org.example.entity.DnaRecord;
import org.example.repository.DnaRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MutantServiceTest {

    @Mock
    private MutantDetector mutantDetector;

    @Mock
    private DnaRecordRepository dnaRecordRepository;

    @InjectMocks
    private MutantService mutantService;

    @Test
    void analyzeDna_ExistingMutant_ReturnsTrue() {
        // Arrange
        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        DnaRecord existingRecord = new DnaRecord();
        existingRecord.setMutant(true);
        
        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(existingRecord);

        // Act
        boolean result = mutantService.analyzeDna(dna);

        // Assert
        assertTrue(result);
        verify(dnaRecordRepository, times(1)).findByDnaHash(anyString());
        verify(mutantDetector, never()).isMutant(any());
        verify(dnaRecordRepository, never()).save(any(DnaRecord.class));
    }

    @Test
    void analyzeDna_ExistingHuman_ReturnsFalse() {
        // Arrange
        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCTTA", "TCACTG"};
        DnaRecord existingRecord = new DnaRecord();
        existingRecord.setMutant(false);

        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(existingRecord);

        // Act
        boolean result = mutantService.analyzeDna(dna);

        // Assert
        assertFalse(result);
        verify(dnaRecordRepository, times(1)).findByDnaHash(anyString());
        verify(mutantDetector, never()).isMutant(any());
        verify(dnaRecordRepository, never()).save(any(DnaRecord.class));
    }

    @Test
    void analyzeDna_NewMutant_ReturnsTrue_AndSaves() {
        // Arrange
        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        
        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(null);
        when(mutantDetector.isMutant(any())).thenReturn(true);

        // Act
        boolean result = mutantService.analyzeDna(dna);

        // Assert
        assertTrue(result);
        verify(mutantDetector, times(1)).isMutant(dna);
        verify(dnaRecordRepository, times(1)).save(argThat(record -> record.isMutant()));
    }

    @Test
    void analyzeDna_NewHuman_ReturnsFalse_AndSaves() {
        // Arrange
        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCTTA", "TCACTG"};
        
        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(null);
        when(mutantDetector.isMutant(any())).thenReturn(false);

        // Act
        boolean result = mutantService.analyzeDna(dna);

        // Assert
        assertFalse(result);
        verify(mutantDetector, times(1)).isMutant(dna);
        verify(dnaRecordRepository, times(1)).save(argThat(record -> !record.isMutant()));
    }

    @Test
    void findAllDnaRecords_ReturnsList() {
        when(dnaRecordRepository.findAll()).thenReturn(java.util.List.of(new DnaRecord()));
        assertFalse(mutantService.findAllDnaRecords().isEmpty());
    }

    @Test
    void findAllMutants_ReturnsList() {
        when(dnaRecordRepository.findByIsMutant(true)).thenReturn(java.util.List.of(new DnaRecord()));
        assertFalse(mutantService.findAllMutants().isEmpty());
    }

    @Test
    void findAllHumans_ReturnsList() {
        when(dnaRecordRepository.findByIsMutant(false)).thenReturn(java.util.List.of(new DnaRecord()));
        assertFalse(mutantService.findAllHumans().isEmpty());
    }

    @Test
    void countAllMutants_ReturnsCount() {
        when(dnaRecordRepository.countByIsMutant(true)).thenReturn(10L);
        assertEquals(10L, mutantService.countAllMutants());
    }

    @Test
    void countAllHumans_ReturnsCount() {
        when(dnaRecordRepository.countByIsMutant(false)).thenReturn(5L);
        assertEquals(5L, mutantService.countAllHumans());
    }

    @Test
    void findByHash_Found_ReturnsRecord() {
        DnaRecord record = new DnaRecord();
        when(dnaRecordRepository.findByDnaHash("hash")).thenReturn(record);
        assertEquals(record, mutantService.findByHash("hash"));
    }

    @Test
    void findByHash_NotFound_ReturnsNull() {
        when(dnaRecordRepository.findByDnaHash("hash")).thenReturn(null);
        assertNull(mutantService.findByHash("hash"));
    }

    @Test
    void analyzeDna_SavesCorrectHash() {
        String[] dna = {"AAAA", "CCCC", "GGGG", "TTTT"};
        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(null);
        when(mutantDetector.isMutant(any())).thenReturn(true);
        
        mutantService.analyzeDna(dna);
        
        verify(dnaRecordRepository).save(argThat(r -> r.getDnaHash() != null && !r.getDnaHash().isEmpty()));
    }

    @Test
    void analyzeDna_SavesCorrectIsMutant() {
        String[] dna = {"AAAA", "CCCC", "GGGG", "TTTT"};
        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(null);
        when(mutantDetector.isMutant(any())).thenReturn(true);
        
        mutantService.analyzeDna(dna);
        
        verify(dnaRecordRepository).save(argThat(DnaRecord::isMutant));
    }

    @Test
    void analyzeDna_SavesCorrectIsHuman() {
        String[] dna = {"AAAA", "CCCC", "GGGG", "TTTT"};
        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(null);
        when(mutantDetector.isMutant(any())).thenReturn(false);
        
        mutantService.analyzeDna(dna);
        
        verify(dnaRecordRepository).save(argThat(r -> !r.isMutant()));
    }

    @Test
    void analyzeDna_SavesWithTimestamp() {
        String[] dna = {"AAAA", "CCCC", "GGGG", "TTTT"};
        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(null);
        when(mutantDetector.isMutant(any())).thenReturn(true);
        
        mutantService.analyzeDna(dna);
        
        verify(dnaRecordRepository).save(argThat(r -> r.getCreatedAt() != null));
    }

    @Test
    void analyzeDna_HandlesExceptionFromDetector() {
        String[] dna = {"AAAA", "CCCC", "GGGG", "TTTT"};
        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(null);
        when(mutantDetector.isMutant(any())).thenThrow(new RuntimeException("Error"));
        
        assertThrows(RuntimeException.class, () -> mutantService.analyzeDna(dna));
    }

    @Test
    void analyzeDna_HandlesExceptionFromRepository() {
        String[] dna = {"AAAA", "CCCC", "GGGG", "TTTT"};
        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(null);
        when(mutantDetector.isMutant(any())).thenReturn(true);
        when(dnaRecordRepository.save(any(DnaRecord.class))).thenThrow(new RuntimeException("DB Error"));
        
        assertThrows(RuntimeException.class, () -> mutantService.analyzeDna(dna));
    }

    @Test
    void analyzeDna_ConsistentHash() {
        String[] dna = {"AAAA", "CCCC", "GGGG", "TTTT"};
        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(null);
        when(mutantDetector.isMutant(any())).thenReturn(true);
        
        mutantService.analyzeDna(dna);
        
        verify(dnaRecordRepository).save(argThat(r -> r.getDnaHash() != null));
    }

    @Test
    void analyzeDna_CallsDetectorOnlyOnce() {
        String[] dna = {"AAAA", "CCCC", "GGGG", "TTTT"};
        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(null);
        when(mutantDetector.isMutant(any())).thenReturn(true);
        
        mutantService.analyzeDna(dna);
        
        verify(mutantDetector, times(1)).isMutant(dna);
    }

    @Test
    void analyzeDna_DoesNotSaveIfFound() {
        String[] dna = {"AAAA", "CCCC", "GGGG", "TTTT"};
        DnaRecord existing = new DnaRecord();
        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(existing);
        
        mutantService.analyzeDna(dna);
        
        verify(dnaRecordRepository, never()).save(any(DnaRecord.class));
    }
}
