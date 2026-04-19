package com.qt.MedTracker.MedicationRecord;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MedicationRecordRepositoryDataJpaTest {

    @Autowired
    private MedicationRecordRepository medicationRecordRepository;

    @Test
    void findByUserIdOrdersByTakenAtDescending() {
        MedicationRecord older = new MedicationRecord(1L, 10, LocalDateTime.of(2026, 4, 18, 8, 0), MedicationRecordStatus.TAKEN);
        older.setSource(MedicationRecordSource.MANUAL);
        MedicationRecord newer = new MedicationRecord(1L, 10, LocalDateTime.of(2026, 4, 19, 8, 0), MedicationRecordStatus.TAKEN);
        newer.setSource(MedicationRecordSource.SLACK);

        medicationRecordRepository.save(older);
        medicationRecordRepository.save(newer);

        assertThat(medicationRecordRepository.findByUserIdOrderByTakenAtDesc(1L))
                .extracting(MedicationRecord::getSource)
                .containsExactly(MedicationRecordSource.SLACK, MedicationRecordSource.MANUAL);
    }
}
