package com.project.firestorm.dto;

public class ActiveUserDTO {
    private String username;
    private String roles;
    private int sessionCount;

    public ActiveUserDTO(String username, String roles, int sessionCount) {
        this.username = username;
        this.roles = roles;
        this.sessionCount = sessionCount;
    }

    public String getUsername() { return username; }
    public String getRoles() { return roles; }
    public int getSessionCount() { return sessionCount; }

}
