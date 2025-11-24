package org.example.repository;

import org.example.entity.DnaRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DnaRecordRepository extends JpaRepository<DnaRecord, Long> {
    public List<DnaRecord> findByIsMutant(boolean isMutant);

    public Long countByIsMutant(boolean isMutant);

    public DnaRecord findByDnaHash(String dnaHash);

}
