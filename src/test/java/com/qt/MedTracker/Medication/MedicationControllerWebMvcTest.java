package com.qt.MedTracker.Medication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qt.MedTracker.SlackAPI.SlackNotificationRequest;
import com.qt.MedTracker.TestSecuritySupport;
import com.qt.MedTracker.common.GlobalExceptionHandler;
import com.qt.MedTracker.security.JwtAuthenticationFilter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MedicationController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class MedicationControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MedicationService medicationService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        TestSecuritySupport.authenticatePatient();
    }

    @AfterEach
    void tearDown() {
        TestSecuritySupport.clearAuthentication();
    }

    @Test
    void userEndpointReturnsOwnMedications() throws Exception {
        Medication medication = new Medication(1, 1L, "Ibuprofen", "200mg", "Twice daily",
                LocalDate.of(2026, 4, 19), LocalDate.of(2026, 4, 25), "Take with food");
        when(medicationService.getMedicationsByUserId(1L)).thenReturn(List.of(medication));

        mockMvc.perform(get("/api/medications/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Ibuprofen"));
    }

    @Test
    void userEndpointRejectsAnotherUsersData() throws Exception {
        mockMvc.perform(get("/api/medications/user/2"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("You do not have access to this user"));
    }

    @Test
    void invalidMedicationPayloadReturnsValidationError() throws Exception {
        mockMvc.perform(post("/api/medications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "",
                                  "start_date": "2026-04-20",
                                  "end_date": "2026-04-19"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Failed"));
    }

    @Test
    void createMedicationUsesCurrentUser() throws Exception {
        Medication medication = new Medication(1, 1L, "Metformin", "500mg", "Twice daily",
                LocalDate.of(2026, 4, 19), LocalDate.of(2026, 5, 19), "Note");
        when(medicationService.createMedication(eq(1L), any(MedicationRequest.class))).thenReturn(medication);

        mockMvc.perform(post("/api/medications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new MedicationRequest("Metformin", "500mg", "Twice daily",
                                        LocalDate.of(2026, 4, 19), LocalDate.of(2026, 5, 19), "Note")
                        )))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.user_id").value(1))
                .andExpect(jsonPath("$.name").value("Metformin"));
    }

    @Test
    void myEndpointReturnsAuthenticatedUsersMedications() throws Exception {
        Medication medication = new Medication(1, 1L, "Metformin", "500mg", "Twice daily",
                LocalDate.of(2026, 4, 19), LocalDate.of(2026, 5, 19), "Note");
        when(medicationService.getMedicationsByUserId(1L)).thenReturn(List.of(medication));

        mockMvc.perform(get("/api/medications/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].user_id").value(1))
                .andExpect(jsonPath("$[0].name").value("Metformin"));
    }

    @Test
    void updateSlackNotificationsUsesCurrentUser() throws Exception {
        Medication medication = new Medication(1, 1L, "Ibuprofen", "200mg", "Twice daily",
                LocalDate.of(2026, 4, 19), LocalDate.of(2026, 4, 25), "Take with food");
        medication.setSlack_notifications_enabled(true);
        when(medicationService.updateSlackNotifications(1L, 1, true)).thenReturn(medication);

        mockMvc.perform(put("/api/medications/1/slack-notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new SlackNotificationRequestBuilder(true))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.slack_notifications_enabled").value(true));
    }

    @Test
    void sendSlackReminderReturnsNoContent() throws Exception {
        doNothing().when(medicationService).sendSlackReminder(1L, 1);

        mockMvc.perform(post("/api/medications/1/slack-reminder"))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateMedicationReturnsUpdatedPayload() throws Exception {
        Medication medication = new Medication(1, 1L, "Metformin", "850mg", "Daily",
                LocalDate.of(2026, 4, 19), LocalDate.of(2026, 5, 19), "Updated");
        when(medicationService.getAccessibleMedication(1, 1L, false)).thenReturn(medication);
        when(medicationService.saveMedication(any(Medication.class))).thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(put("/api/medications/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new MedicationRequest("Metformin", "850mg", "Daily",
                                        LocalDate.of(2026, 4, 19), LocalDate.of(2026, 5, 19), "Updated")
                        )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dosage").value("850mg"))
                .andExpect(jsonPath("$.frequency").value("Daily"));
    }

    @Test
    void deleteMedicationReturnsOk() throws Exception {
        doNothing().when(medicationService).deleteMedicationForUser(1, 1L, false);

        mockMvc.perform(delete("/api/medications/1"))
                .andExpect(status().isOk());
    }

    @Test
    void adminCanReadAnotherUsersMedications() throws Exception {
        TestSecuritySupport.authenticateAdmin();
        Medication medication = new Medication(2, 2L, "Lisinopril", "10mg", "Daily",
                LocalDate.of(2026, 4, 19), LocalDate.of(2026, 5, 19), "Take in the morning");
        when(medicationService.getMedicationsByUserId(2L)).thenReturn(List.of(medication));

        mockMvc.perform(get("/api/medications/user/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].user_id").value(2))
                .andExpect(jsonPath("$[0].name").value("Lisinopril"));
    }

    private record SlackNotificationRequestBuilder(boolean enabled) {
    }
}
