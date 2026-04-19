package com.qt.MedTracker.SlackAPI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
@RequestMapping("/api/slack")
public class SlackInteractionController {

    private final SlackInteractionService slackInteractionService;

    public SlackInteractionController(SlackInteractionService slackInteractionService) {
        this.slackInteractionService = slackInteractionService;
    }

    @PostMapping("/interactions")
    public ResponseEntity<?> handleInteraction(@RequestBody String rawBody,
                                               @RequestHeader("X-Slack-Signature") String signature,
                                               @RequestHeader("X-Slack-Request-Timestamp") String timestamp) {
        String payload = extractPayload(rawBody);
        slackInteractionService.verifyRequest(signature, timestamp, rawBody);
        Map<String, Object> responseBody = slackInteractionService.handleInteraction(payload);
        if (responseBody == null || responseBody.isEmpty()) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.ok(responseBody);
    }

    private String extractPayload(String rawBody) {
        if (rawBody == null || !rawBody.startsWith("payload=")) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "Slack payload is missing");
        }

        return URLDecoder.decode(rawBody.substring("payload=".length()), StandardCharsets.UTF_8);
    }
}
