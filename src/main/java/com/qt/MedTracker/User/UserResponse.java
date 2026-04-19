package com.qt.MedTracker.User;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserResponse(
        Long id,
        String name,
        Integer age,
        String email,
        String role,
        @JsonProperty("slack_connected") boolean slackConnected,
        @JsonProperty("slack_member_id") String slackMemberId
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getAge(),
                user.getEmail(),
                user.getRole(),
                user.isSlackConnected(),
                user.getSlackMemberId()
        );
    }
}
