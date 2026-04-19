package com.qt.MedTracker.SlackAPI;

import com.qt.MedTracker.common.GlobalExceptionHandler;
import com.qt.MedTracker.security.JwtAuthenticationFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Map;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SlackInteractionController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class SlackInteractionControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SlackInteractionService slackInteractionService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void validSlackInteractionReturnsServicePayload() throws Exception {
        String payload = "{\"type\":\"block_actions\",\"actions\":[{\"action_id\":\"medication_taken\",\"value\":\"1:3\"}]}";
        String encodedBody = "payload=" + URLEncoder.encode(payload, StandardCharsets.UTF_8);
        String timestamp = String.valueOf(Instant.now().getEpochSecond());

        doNothing().when(slackInteractionService).verifyRequest("sig", timestamp, encodedBody);
        when(slackInteractionService.handleInteraction(payload)).thenReturn(Map.of("text", "Medication recorded."));

        mockMvc.perform(post("/api/slack/interactions")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(encodedBody)
                        .header("X-Slack-Signature", "sig")
                        .header("X-Slack-Request-Timestamp", timestamp))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Medication recorded."));
    }

    @Test
    void invalidSignatureReturnsUnauthorizedApiError() throws Exception {
        String payload = "{\"type\":\"block_actions\",\"actions\":[]}";
        String encodedBody = "payload=" + URLEncoder.encode(payload, StandardCharsets.UTF_8);
        String timestamp = String.valueOf(Instant.now().getEpochSecond());

        doThrow(new ResponseStatusException(UNAUTHORIZED, "Invalid Slack signature"))
                .when(slackInteractionService)
                .verifyRequest("bad", timestamp, encodedBody);

        mockMvc.perform(post("/api/slack/interactions")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(encodedBody)
                        .header("X-Slack-Signature", "bad")
                        .header("X-Slack-Request-Timestamp", timestamp))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid Slack signature"));
    }

    @Test
    void missingPayloadReturnsUnifiedBadRequestError() throws Exception {
        String rawBody = "token=abc";
        String timestamp = String.valueOf(Instant.now().getEpochSecond());

        mockMvc.perform(post("/api/slack/interactions")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(rawBody)
                        .header("X-Slack-Signature", "sig")
                        .header("X-Slack-Request-Timestamp", timestamp))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Slack payload is missing"));
    }
}
