package com.dailycodebuffer.cricket.model;

import lombok.Data;

@Data
public class PlayerResponse {
    private Long playerId;
    private String name;
    private String role;

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    // Setter for name
    public void setName(String name) {
        this.name = name;
    }

    // Setter for role
    public void setRole(String role) {
        this.role = role;
    }

    // Getter for playerId
    public Long getPlayerId() {
        return playerId;
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Getter for role
    public String getRole() {
        return role;
    }
}
