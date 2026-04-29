package com.marketplace.backend.dto.response;

public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private String userId;
    private String email;
    private String nom;
    private String prenom;
    private String role;

    public AuthResponse() {}

    public AuthResponse(String accessToken, String refreshToken, String userId,
                        String email, String nom, String prenom, String role) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userId = userId;
        this.email = email;
        this.nom = nom;
        this.prenom = prenom;
        this.role = role;
    }

    // Builder manuel
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String accessToken, refreshToken, userId, email, nom, prenom, role;
        public Builder accessToken(String v) { this.accessToken = v; return this; }
        public Builder refreshToken(String v) { this.refreshToken = v; return this; }
        public Builder userId(String v) { this.userId = v; return this; }
        public Builder email(String v) { this.email = v; return this; }
        public Builder nom(String v) { this.nom = v; return this; }
        public Builder prenom(String v) { this.prenom = v; return this; }
        public Builder role(String v) { this.role = v; return this; }
        public AuthResponse build() {
            return new AuthResponse(accessToken, refreshToken, userId, email, nom, prenom, role);
        }
    }

    // Getters
    public String getAccessToken() { return accessToken; }
    public String getRefreshToken() { return refreshToken; }
    public String getUserId() { return userId; }
    public String getEmail() { return email; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getRole() { return role; }
}