package com.qt.MedTracker.SlackAPI;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class SlackConnectionRequest {
    @JsonProperty("slack_member_id")
    @NotBlank(message = "slack_member_id is required")
    @Pattern(regexp = "^[A-Z0-9]{6,20}$", message = "slack_member_id must look like a Slack member ID")
    private String slackMemberId;

    public SlackConnectionRequest() {
    }

    public String getSlackMemberId() {
        return slackMemberId;
    }

    public void setSlackMemberId(String slackMemberId) {
        this.slackMemberId = slackMemberId;
    }
}
