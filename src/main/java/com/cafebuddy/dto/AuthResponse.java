package com.cafebuddy.dto;

public class AuthResponse {
    private String token;
    private String type;
    private Long id;
    private String fullName;
    private String email;
    private String role;

    public AuthResponse() {}

    public AuthResponse(String token, String type, Long id,
                        String fullName, String email, String role) {
        this.token = token; this.type = type; this.id = id;
        this.fullName = fullName; this.email = email; this.role = role;
    }

    public String getToken() { return token; }
    public String getType() { return type; }
    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getRole() { return role; }

    public void setToken(String token) { this.token = token; }
    public void setType(String type) { this.type = type; }
    public void setId(Long id) { this.id = id; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setEmail(String email) { this.email = email; }
    public void setRole(String role) { this.role = role; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String token, type, fullName, email, role;
        private Long id;

        public Builder token(String token) { this.token = token; return this; }
        public Builder type(String type) { this.type = type; return this; }
        public Builder id(Long id) { this.id = id; return this; }
        public Builder fullName(String fullName) { this.fullName = fullName; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder role(String role) { this.role = role; return this; }

        public AuthResponse build() {
            return new AuthResponse(token, type, id, fullName, email, role);
        }
    }
}
