package com.qt.MedTracker.SlackAPI;

import com.qt.MedTracker.Medication.Medication;
import com.qt.MedTracker.User.User;
import com.qt.MedTracker.notification.NotificationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
public class SlackService implements NotificationService {

    @Value("${slack.bot-token:}")
    private String botToken;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void sendMedicationReminder(User user, Medication medication) {
        if (botToken == null || botToken.isBlank()) {
            throw new IllegalStateException("Slack bot token is not configured");
        }

        postToSlackApi(
                "https://slack.com/api/chat.postMessage",
                Map.of(
                        "channel", user.getSlackMemberId(),
                        "text", "Medication reminder for " + user.getName(),
                        "blocks", java.util.List.of(
                                Map.of(
                                        "type", "section",
                                        "text", Map.of(
                                                "type", "mrkdwn",
                                                "text", "*Medication reminder*\n"
                                                        + user.getName()
                                                        + ", it's time to take *"
                                                        + medication.getName()
                                                        + "* ("
                                                        + medication.getDosage()
                                                        + ").\nFrequency: "
                                                        + medication.getFrequency()
                                        )
                                ),
                                Map.of(
                                        "type", "actions",
                                        "elements", java.util.List.of(
                                                Map.of(
                                                        "type", "button",
                                                        "action_id", "medication_taken",
                                                        "text", Map.of(
                                                                "type", "plain_text",
                                                                "text", "Taken"
                                                        ),
                                                        "style", "primary",
                                                        "value", user.getId() + ":" + medication.getId()
                                                )
                                        )
                                )
                        )
                )
        );
    }

    public void updateInteractiveReminder(String channelId, String messageTs, String userName, String medicationName) {
        postToSlackApi(
                "https://slack.com/api/chat.update",
                Map.of(
                        "channel", channelId,
                        "ts", messageTs,
                        "text", "Medication recorded for " + userName,
                        "blocks", java.util.List.of(
                                Map.of(
                                        "type", "section",
                                        "text", Map.of(
                                                "type", "mrkdwn",
                                                "text", ":white_check_mark: Recorded *" + medicationName + "* as taken for " + userName + "."
                                        )
                                )
                        )
                )
        );
    }

    private void postToSlackApi(String url, Object payload) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(botToken);

        HttpEntity<Object> request = new HttpEntity<>(payload, headers);

        try {
            restTemplate.postForEntity(url, request, String.class);
        } catch (Exception e) {
            throw new IllegalStateException("Slack API request failed: " + e.getMessage(), e);
        }
    }
}
