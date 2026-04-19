package com.qt.MedTracker.Medication;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Configuration
public class MedicationConfig {
    private static final long MIN_SAMPLE_MEDICATIONS = 8;

    @Bean
    CommandLineRunner initMedicationData(MedicationRepository repository) {
        return args -> {
            deduplicateMedications(repository);

            List<Medication> sampleMedications = List.of(
                    new Medication(
                            1L,
                            "Ibuprofen",
                            "200mg",
                            "Twice a day",
                            LocalDate.now().minusDays(2),
                            LocalDate.now().plusDays(5),
                            "Take with food"
                    ),
                    new Medication(
                            2L,
                            "Paracetamol",
                            "500mg",
                            "Once a day",
                            LocalDate.now(),
                            LocalDate.now().plusDays(10),
                            "Avoid alcohol"
                    ),
                    new Medication(
                            1L,
                            "Lisinopril",
                            "10mg",
                            "Every morning",
                            LocalDate.now().minusDays(12),
                            LocalDate.now().plusDays(18),
                            "Monitor blood pressure readings"
                    ),
                    new Medication(
                            1L,
                            "Vitamin D",
                            "1000 IU",
                            "Daily",
                            LocalDate.now().minusDays(30),
                            LocalDate.now().plusDays(60),
                            "Take after breakfast"
                    ),
                    new Medication(
                            2L,
                            "Atorvastatin",
                            "20mg",
                            "Every night",
                            LocalDate.now().minusDays(20),
                            LocalDate.now().plusDays(25),
                            "Avoid grapefruit"
                    ),
                    new Medication(
                            2L,
                            "Omeprazole",
                            "20mg",
                            "Before breakfast",
                            LocalDate.now().minusDays(5),
                            LocalDate.now().plusDays(15),
                            "Take on an empty stomach"
                    ),
                    new Medication(
                            1L,
                            "Metformin XR",
                            "750mg",
                            "With dinner",
                            LocalDate.now().minusDays(8),
                            LocalDate.now().plusDays(40),
                            "Helps smooth evening glucose levels"
                    ),
                    new Medication(
                            2L,
                            "Cetirizine",
                            "10mg",
                            "As needed",
                            LocalDate.now().minusDays(3),
                            LocalDate.now().plusDays(14),
                            "Use during allergy flare-ups"
                    )
            );

            Map<String, Medication> existingMedicationMap = new LinkedHashMap<>();
            for (Medication medication : repository.findAll()) {
                existingMedicationMap.put(buildMedicationKey(medication), medication);
            }

            List<Medication> medicationsToInsert = sampleMedications.stream()
                    .filter(medication -> !existingMedicationMap.containsKey(buildMedicationKey(medication)))
                    .limit(Math.max(0, MIN_SAMPLE_MEDICATIONS - repository.count()))
                    .toList();

            if (!medicationsToInsert.isEmpty()) {
                repository.saveAll(medicationsToInsert);
            }
        };
    }

    private void deduplicateMedications(MedicationRepository repository) {
        Map<String, Medication> uniqueMedicationMap = new LinkedHashMap<>();
        List<Medication> duplicateMedications = new java.util.ArrayList<>();

        for (Medication medication : repository.findAll()) {
            String key = buildMedicationKey(medication);
            Medication existing = uniqueMedicationMap.putIfAbsent(key, medication);

            if (existing != null) {
                duplicateMedications.add(medication);
            }
        }

        if (!duplicateMedications.isEmpty()) {
            repository.deleteAll(duplicateMedications);
        }
    }

    private String buildMedicationKey(Medication medication) {
        return String.join("|",
                String.valueOf(medication.getUser_id()),
                Objects.toString(medication.getName(), ""),
                Objects.toString(medication.getDosage(), ""),
                Objects.toString(medication.getFrequency(), ""),
                Objects.toString(medication.getStart_date(), ""),
                Objects.toString(medication.getEnd_date(), ""),
                Objects.toString(medication.getNote(), "")
        );
    }
}
