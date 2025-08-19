package com.dailycodebuffer.cricket.model;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PlayerRequest {

    @NotNull
    @NotEmpty
    @Size(min = 3, max = 50, message = "Name length must be between 3 and 50")
    private String name;
    @NotNull
    @NotEmpty
    @Size(min = 3, max = 50, message = "Role length must be between 3 and 50")
    private String role;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

