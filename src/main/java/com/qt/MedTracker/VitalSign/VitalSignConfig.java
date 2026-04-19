package com.qt.MedTracker.VitalSign;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Configuration
public class VitalSignConfig {
    private static final long MIN_SAMPLE_VITAL_SIGNS = 8;

    @Bean
    CommandLineRunner initVitalSignData(VitalSignRepository repository, JdbcTemplate jdbcTemplate) {
        return args -> {
            jdbcTemplate.update("""
                    UPDATE vital_signs
                    SET recorded_on = COALESCE(recorded_on, NULLIF(date, '')::date),
                        body_temperature_c = COALESCE(body_temperature_c, NULLIF(body_temperature, '')::numeric),
                        heart_rate_bpm = COALESCE(heart_rate_bpm, NULLIF(heart_rate, '')::integer),
                        blood_pressure_systolic = COALESCE(blood_pressure_systolic, NULLIF(split_part(blood_pressure, '/', 1), '')::integer),
                        blood_pressure_diastolic = COALESCE(blood_pressure_diastolic, NULLIF(split_part(blood_pressure, '/', 2), '')::integer),
                        blood_sugar_mg_dl = COALESCE(blood_sugar_mg_dl, NULLIF(blood_sugar, '')::integer)
                    WHERE recorded_on IS NULL
                       OR body_temperature_c IS NULL
                       OR heart_rate_bpm IS NULL
                       OR blood_pressure_systolic IS NULL
                       OR blood_pressure_diastolic IS NULL
                       OR blood_sugar_mg_dl IS NULL
                    """);

            if (repository.count() >= MIN_SAMPLE_VITAL_SIGNS) {
                return;
            }

            List<VitalSign> sampleVitalSigns = List.of(
                    new VitalSign(1L, LocalDate.now().minusDays(6), new BigDecimal("36.7"), 70, 117, 75, 94),
                    new VitalSign(1L, LocalDate.now().minusDays(5), new BigDecimal("36.8"), 72, 118, 76, 96),
                    new VitalSign(1L, LocalDate.now().minusDays(4), new BigDecimal("36.9"), 74, 119, 77, 98),
                    new VitalSign(1L, LocalDate.now().minusDays(3), new BigDecimal("36.8"), 73, 120, 78, 97),
                    new VitalSign(1L, LocalDate.now().minusDays(2), new BigDecimal("37.0"), 75, 121, 79, 101),
                    new VitalSign(1L, LocalDate.now().minusDays(1), new BigDecimal("36.9"), 71, 118, 76, 95),
                    new VitalSign(2L, LocalDate.now().minusDays(3), new BigDecimal("36.6"), 68, 114, 72, 92),
                    new VitalSign(2L, LocalDate.now(), new BigDecimal("36.7"), 69, 115, 73, 93)
            );

            long missingCount = MIN_SAMPLE_VITAL_SIGNS - repository.count();
            repository.saveAll(sampleVitalSigns.stream()
                    .limit(Math.max(0, missingCount))
                    .toList());
        };
    }
}
