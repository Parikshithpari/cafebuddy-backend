package com.cafebuddy.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.Instant;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 80)
    @Column(nullable = false, length = 80)
    private String fullName;

    @NotBlank
    @Email
    @Size(max = 120)
    @Column(nullable = false, unique = true, length = 120)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @Size(max = 20)
    @Column(length = 20)
    private String phoneNumber;

    @Column(nullable = false, length = 20)
    private String role = "USER";

    @Column(nullable = false)
    private Boolean enabled = true;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    public User() {}

    public User(Long id, String fullName, String email, String password, String phoneNumber,
                String role, Boolean enabled, Instant createdAt) {
        this.id = id; this.fullName = fullName; this.email = email;
        this.password = password; this.phoneNumber = phoneNumber;
        this.role = role; this.enabled = enabled; this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getRole() { return role; }
    public Boolean getEnabled() { return enabled; }
    public Instant getCreatedAt() { return createdAt; }

    public void setId(Long id) { this.id = id; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setRole(String role) { this.role = role; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String fullName, email, password, phoneNumber;
        private String role = "USER";
        private Boolean enabled = true;
        private Instant createdAt = Instant.now();

        public Builder id(Long id) { this.id = id; return this; }
        public Builder fullName(String n) { this.fullName = n; return this; }
        public Builder email(String e) { this.email = e; return this; }
        public Builder password(String p) { this.password = p; return this; }
        public Builder phoneNumber(String p) { this.phoneNumber = p; return this; }
        public Builder role(String r) { this.role = r; return this; }
        public Builder enabled(Boolean e) { this.enabled = e; return this; }
        public Builder createdAt(Instant c) { this.createdAt = c; return this; }

        public User build() {
            return new User(id, fullName, email, password, phoneNumber, role, enabled, createdAt);
        }
    }
}
