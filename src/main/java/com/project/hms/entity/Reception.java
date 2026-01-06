package com.project.hms.entity;

import jakarta.persistence.*;

@Entity
public class Reception {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long receptionId;

    @Column(unique = true)
    private String username;
    private String password;
    private String email;
    private Boolean isActive;

    public Reception() {}

    public Reception(Long receptionId, String username, String password, String email, Boolean isActive) {
        this.receptionId = receptionId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.isActive = isActive;
    }

    public Long getReceptionId() {
        return receptionId;
    }

    public void setReceptionId(Long receptionId) {
        this.receptionId = receptionId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "Reception{" +
                "receptionId=" + receptionId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
