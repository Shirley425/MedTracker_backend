package com.qt.MedTracker.Medication;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Integer> {
    List<Medication> findByUserId(Long userId);
    java.util.Optional<Medication> findByIdAndUserId(Integer id, Long userId);
}
