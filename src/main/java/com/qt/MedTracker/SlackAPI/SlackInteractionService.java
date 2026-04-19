package com.qt.MedTracker.SlackAPI;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qt.MedTracker.Medication.Medication;
import com.qt.MedTracker.Medication.MedicationService;
import com.qt.MedTracker.MedicationRecord.MedicationRecordRequest;
import com.qt.MedTracker.MedicationRecord.MedicationRecordService;
import com.qt.MedTracker.User.User;
import com.qt.MedTracker.User.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;

@Service
public class SlackInteractionService {

    private final ObjectMapper objectMapper;
    private final UserService userService;
    private final MedicationService medicationService;
    private final MedicationRecordService medicationRecordService;
    private final SlackService slackService;
    private final String signingSecret;

    public SlackInteractionService(ObjectMapper objectMapper,
                                   UserService userService,
                                   MedicationService medicationService,
                                   MedicationRecordService medicationRecordService,
                                   SlackService slackService,
                                   @Value("${slack.signing-secret:}") String signingSecret) {
        this.objectMapper = objectMapper;
        this.userService = userService;
        this.medicationService = medicationService;
        this.medicationRecordService = medicationRecordService;
        this.slackService = slackService;
        this.signingSecret = signingSecret;
    }

    public void verifyRequest(String signature, String timestamp, String rawBody) {
        if (signingSecret == null || signingSecret.isBlank()) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Slack signing secret is not configured");
        }

        long requestTs = Long.parseLong(timestamp);
        long now = Instant.now().getEpochSecond();
        if (Math.abs(now - requestTs) > 60L * 5) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Slack request timestamp is too old");
        }

        String expected = "v0=" + hmacSha256("v0:" + timestamp + ":" + rawBody, signingSecret);
        if (!expected.equals(signature)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Slack signature");
        }
    }

    public Map<String, Object> handleInteraction(String payload) {
        try {
            Map<String, Object> body = objectMapper.readValue(payload, new TypeReference<>() {
            });
            if (!"block_actions".equals(body.get("type"))) {
                return Map.of();
            }

            List<Map<String, Object>> actions = (List<Map<String, Object>>) body.get("actions");
            if (actions == null || actions.isEmpty()) {
                return Map.of();
            }

            Map<String, Object> action = actions.get(0);
            if (!"medication_taken".equals(action.get("action_id"))) {
                return Map.of();
            }

            String value = String.valueOf(action.get("value"));
            String[] parts = value.split(":");
            Long expectedUserId = Long.valueOf(parts[0]);
            Integer medicationId = Integer.valueOf(parts[1]);

            Map<String, Object> slackUser = (Map<String, Object>) body.get("user");
            String slackMemberId = String.valueOf(slackUser.get("id"));
            User user = userService.getUserBySlackMemberId(slackMemberId);
            if (!user.getId().equals(expectedUserId)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Slack user does not match the linked MedTracker account");
            }

            Medication medication = medicationService.getMedicationForUserOrThrow(medicationId, user.getId());
            medicationRecordService.createTakenRecord(
                    user.getId(),
                    new MedicationRecordRequest(medicationId, "TAKEN", "SLACK")
            );

            Map<String, Object> container = (Map<String, Object>) body.get("container");
            Map<String, Object> channel = (Map<String, Object>) body.get("channel");
            slackService.updateInteractiveReminder(
                    String.valueOf(channel.get("id")),
                    String.valueOf(container.get("message_ts")),
                    user.getName(),
                    medication.getName()
            );

            return Map.of("text", "Medication recorded.");
        } catch (ResponseStatusException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unable to process Slack interaction");
        }
    }

    private String hmacSha256(String value, String secret) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return HexFormat.of().formatHex(mac.doFinal(value.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to verify Slack signature", exception);
        }
    }
}
