package org.example.service;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.example.entity.DnaRecord;
import org.example.repository.DnaRecordRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    public Long countAllHumans(){
        return dnaRecordRepository.countByIsMutant(false);
    }
    public Long countAllMutants(){
        return dnaRecordRepository.countByIsMutant(true);
    }
    public List<DnaRecord> findAllHumans(){
        return dnaRecordRepository.findByIsMutant(false);
    }

    public DnaRecord findByHash(String dnaHash){
        return dnaRecordRepository.findByDnaHash(dnaHash);
    }
    public boolean analyzeDna(@org.jetbrains.annotations.NotNull String[] dna){

        String dnaHash = hashDna(dna);
        DnaRecord dnaBuscado= dnaRecordRepository.findByDnaHash(dnaHash);
        if(dnaBuscado != null){
            return dnaBuscado.isMutant();
        }else{
            boolean isMutant = mutantDetector.isMutant(dna);
            dnaRecordRepository.save(
                    DnaRecord.builder()
                            .dnaHash(dnaHash)
                            .createdAt(LocalDateTime.now())
                            .isMutant(isMutant)
                            .build()
            );
            return isMutant;
        }
    }
    private String hashDna(String[] dna){
        String dnaSequence = String.join("", dna);
        return DigestUtils.sha256Hex(dnaSequence);
    }
}
