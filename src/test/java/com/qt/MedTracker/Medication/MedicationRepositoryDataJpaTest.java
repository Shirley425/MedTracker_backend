package com.qt.MedTracker.Medication;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MedicationRepositoryDataJpaTest {

    @Autowired
    private MedicationRepository medicationRepository;

    @Test
    void findsMedicationByUserAndScopedId() {
        Medication medication = new Medication(
                1L,
                "Lisinopril",
                "10mg",
                "Daily",
                LocalDate.of(2026, 4, 19),
                LocalDate.of(2026, 5, 19),
                "Morning dose"
        );

        Medication saved = medicationRepository.save(medication);

        assertThat(medicationRepository.findByUserId(1L))
                .extracting(Medication::getName)
                .containsExactly("Lisinopril");
        assertThat(medicationRepository.findByIdAndUserId(saved.getId(), 1L)).isPresent();
        assertThat(medicationRepository.findByIdAndUserId(saved.getId(), 2L)).isEmpty();
    }
}
