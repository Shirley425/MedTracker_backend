package com.qt.MedTracker.VitalSign;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class VitalSignConfig {
    private static final long MIN_SAMPLE_VITAL_SIGNS = 8;

    @Bean
    CommandLineRunner initVitalSignData(VitalSignRepository repository) {
        return args -> {
            if (repository.count() >= MIN_SAMPLE_VITAL_SIGNS) {
                return;
            }

            List<VitalSign> sampleVitalSigns = List.of(
                    new VitalSign(1L, LocalDate.now().minusDays(6).toString(), "36.7", "70", "117/75", "94"),
                    new VitalSign(1L, LocalDate.now().minusDays(5).toString(), "36.8", "72", "118/76", "96"),
                    new VitalSign(1L, LocalDate.now().minusDays(4).toString(), "36.9", "74", "119/77", "98"),
                    new VitalSign(1L, LocalDate.now().minusDays(3).toString(), "36.8", "73", "120/78", "97"),
                    new VitalSign(1L, LocalDate.now().minusDays(2).toString(), "37.0", "75", "121/79", "101"),
                    new VitalSign(1L, LocalDate.now().minusDays(1).toString(), "36.9", "71", "118/76", "95"),
                    new VitalSign(2L, LocalDate.now().minusDays(3).toString(), "36.6", "68", "114/72", "92"),
                    new VitalSign(2L, LocalDate.now().toString(), "36.7", "69", "115/73", "93")
            );

            long missingCount = MIN_SAMPLE_VITAL_SIGNS - repository.count();
            repository.saveAll(sampleVitalSigns.stream()
                    .limit(Math.max(0, missingCount))
                    .toList());
        };
    }
}
