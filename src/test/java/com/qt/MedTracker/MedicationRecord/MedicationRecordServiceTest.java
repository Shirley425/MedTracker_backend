package com.qt.MedTracker.MedicationRecord;

import com.qt.MedTracker.Medication.Medication;
import com.qt.MedTracker.Medication.MedicationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MedicationRecordServiceTest {

    @Mock
    private MedicationRecordRepository medicationRecordRepository;

    @Mock
    private MedicationService medicationService;

    @InjectMocks
    private MedicationRecordService medicationRecordService;

    @Test
    void createTakenRecordDefaultsStatusAndSourceWhenMissing() {
        Medication medication = new Medication(1, 1L, "Ibuprofen", "200mg", "Twice daily",
                LocalDate.now(), LocalDate.now().plusDays(3), "Note");
        when(medicationService.getMedicationForUserOrThrow(1, 1L)).thenReturn(medication);
        when(medicationRecordRepository.save(any(MedicationRecord.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MedicationRecord saved = medicationRecordService.createTakenRecord(1L, new MedicationRecordRequest(1, null, null));

        assertThat(saved.getStatus()).isEqualTo(MedicationRecordStatus.TAKEN);
        assertThat(saved.getSource()).isEqualTo(MedicationRecordSource.MANUAL);
        assertThat(saved.getTaken_at()).isBeforeOrEqualTo(LocalDateTime.now());

        ArgumentCaptor<MedicationRecord> captor = ArgumentCaptor.forClass(MedicationRecord.class);
        verify(medicationRecordRepository).save(captor.capture());
        assertThat(captor.getValue().getMedication_id()).isEqualTo(1);
    }
}
