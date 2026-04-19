package com.qt.MedTracker.SlackAPI;

public class SlackWebhookRequest {
    private String webhookUrl;

    public SlackWebhookRequest() {
    }

    public String getWebhookUrl() {
        return webhookUrl;
    }

    public void setWebhookUrl(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }
}
