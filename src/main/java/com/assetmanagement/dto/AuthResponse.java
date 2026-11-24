package com.assetmanagement.dto;

import com.assetmanagement.model.Role;

public class AuthResponse {
    private String token;
    private String username;
    private String email;
    private Role role;
    private String teamName;

    public AuthResponse() {}
    public AuthResponse(String token, String username, String email, Role role, String teamName) {
        this.token = token;
        this.username = username;
        this.email = email;
        this.role = role;
        this.teamName = teamName;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }
}
