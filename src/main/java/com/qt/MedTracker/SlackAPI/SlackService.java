package com.qt.MedTracker.SlackAPI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SlackService {

    @Value("${slack.webhook.url}")
    private String webhookUrl;
    @Value("${app.frontend.url:http://127.0.0.1:3000}")
    private String frontendUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendSlackMessage(String message) {
        if (webhookUrl == null || webhookUrl.isBlank()) {
            return;
        }

        Map<String, String> payload = new HashMap<>();
        payload.put("text", message);
        sendToWebhook(webhookUrl, payload);
    }

    public void sendMedicationReminder(String targetWebhookUrl,
                                       String userName,
                                       Integer medicationId,
                                       String medicationName,
                                       String dosage,
                                       String frequency) {
        if (targetWebhookUrl == null || targetWebhookUrl.isBlank()) {
            return;
        }

        Map<String, Object> payload = new HashMap<>();
        payload.put("text", "Medication reminder for " + userName);

        List<Map<String, Object>> blocks = new ArrayList<>();
        blocks.add(Map.of(
                "type", "section",
                "text", Map.of(
                        "type", "mrkdwn",
                        "text", "*Medication reminder*\n" +
                                userName + ", it's time to take *" + medicationName + "* (" + dosage + ").\n" +
                                "Frequency: " + frequency
                )
        ));
        blocks.add(Map.of(
                "type", "actions",
                "elements", List.of(
                        Map.of(
                                "type", "button",
                                "text", Map.of(
                                        "type", "plain_text",
                                        "text", "Taken"
                                ),
                                "style", "primary",
                                "url", frontendUrl + "/dashboard?action=taken&medicationId=" + medicationId
                        )
                )
        ));
        payload.put("blocks", blocks);

        sendToWebhook(targetWebhookUrl, payload);
    }

    private void sendToWebhook(String targetWebhookUrl, Object payload) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> request = new HttpEntity<>(payload, headers);

        try {
            restTemplate.postForEntity(targetWebhookUrl, request, String.class);
        } catch (Exception e) {
            System.out.println("Slack notification sent error: " + e.getMessage());
        }
    }
}
