package org.example.service;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.example.entity.DnaRecord;
import org.example.repository.DnaRecordRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@EqualsAndHashCode
public class MutantService {
    private final MutantDetector mutantDetector;
    private final DnaRecordRepository dnaRecordRepository;

    public List<DnaRecord> findAllDnaRecords(){
        return dnaRecordRepository.findAll();
    }

    public List<DnaRecord> findAllMutants(){
        return dnaRecordRepository.findByIsMutant(true);
    }

    public List<DnaRecord> findAllHumans(){
        return dnaRecordRepository.findByIsMutant(false);
    }

    public DnaRecord findByHash(int dnaHash){
        return dnaRecordRepository.findByDnaHash(dnaHash);
    }
    public boolean analyzeDna(@org.jetbrains.annotations.NotNull String[] dna){

        int dnaHash = java.util.Arrays.deepHashCode(dna);
        DnaRecord dnaBuscado= dnaRecordRepository.findByDnaHash(dnaHash);
        if(dnaBuscado != null){
            return dnaBuscado.isMutant();
        }else{
            boolean isMutant = mutantDetector.isMutant(dna);
            dnaRecordRepository.save(
                    DnaRecord.builder()
                            .dnaHash(dnaHash)
                            .isMutant(isMutant)
                            .build()
            );
        }
        return true;
    }

}
