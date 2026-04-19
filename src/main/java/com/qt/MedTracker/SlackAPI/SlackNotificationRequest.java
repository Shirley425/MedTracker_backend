package com.qt.MedTracker.SlackAPI;

public class SlackNotificationRequest {
    private boolean enabled;

    public SlackNotificationRequest() {
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
