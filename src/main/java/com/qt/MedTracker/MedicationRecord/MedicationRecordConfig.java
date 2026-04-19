package com.qt.MedTracker.MedicationRecord;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class MedicationRecordConfig {

    @Bean
    CommandLineRunner normalizeMedicationRecordData(JdbcTemplate jdbcTemplate) {
        return args -> jdbcTemplate.update(
                "UPDATE medication_records SET source = 'MANUAL' WHERE source IS NULL"
        );
    }
}
