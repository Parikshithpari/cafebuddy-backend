package com.cafebuddy.dto;

import java.time.Instant;

public class UserDto {
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String role;
    private Boolean enabled;
    private Instant createdAt;

    public UserDto() {}

    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getRole() { return role; }
    public Boolean getEnabled() { return enabled; }
    public Instant getCreatedAt() { return createdAt; }

    public void setId(Long id) { this.id = id; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setEmail(String email) { this.email = email; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setRole(String role) { this.role = role; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String fullName, email, phoneNumber, role;
        private Boolean enabled;
        private Instant createdAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder fullName(String v) { this.fullName = v; return this; }
        public Builder email(String v) { this.email = v; return this; }
        public Builder phoneNumber(String v) { this.phoneNumber = v; return this; }
        public Builder role(String v) { this.role = v; return this; }
        public Builder enabled(Boolean v) { this.enabled = v; return this; }
        public Builder createdAt(Instant v) { this.createdAt = v; return this; }

        public UserDto build() {
            UserDto d = new UserDto();
            d.id = id; d.fullName = fullName; d.email = email;
            d.phoneNumber = phoneNumber; d.role = role;
            d.enabled = enabled; d.createdAt = createdAt;
            return d;
        }
    }
}
