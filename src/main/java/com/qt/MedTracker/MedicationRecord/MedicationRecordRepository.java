package com.qt.MedTracker.MedicationRecord;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicationRecordRepository extends JpaRepository<MedicationRecord, Integer> {
    List<MedicationRecord> findByUserIdOrderByTakenAtDesc(Long userId);
}
