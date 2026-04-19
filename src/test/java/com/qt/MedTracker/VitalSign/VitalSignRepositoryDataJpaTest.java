package com.qt.MedTracker.VitalSign;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class VitalSignRepositoryDataJpaTest {

    @Autowired
    private VitalSignRepository vitalSignRepository;

    @Test
    void savesAndQueriesNormalizedVitalSignFields() {
        VitalSign vitalSign = new VitalSign(
                1L,
                LocalDate.of(2026, 4, 19),
                new BigDecimal("36.8"),
                72,
                118,
                76,
                96
        );

        VitalSign saved = vitalSignRepository.save(vitalSign);

        VitalSign reloaded = vitalSignRepository.findByIdAndUserId(saved.getId(), 1L).orElseThrow();
        assertThat(reloaded.getHeart_rate()).isEqualTo(72);
        assertThat(reloaded.getBloodPressureSystolic()).isEqualTo(118);
        assertThat(reloaded.getBlood_pressure()).isEqualTo("118/76");
    }
}
