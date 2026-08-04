package com.armi.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password = "123456"; // Default password
    private String role; // "ADMIN" or "DRIVER"
    private String phone; // WhatsApp contact e.g. "3248113411"
    private Double accumulatedEarnings = 0.0;

    public AppUser() {}

    public AppUser(String name, String email, String role) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.accumulatedEarnings = 0.0;
        this.password = "123456";
    }

    public AppUser(String name, String email, String password, String role) {
        this.name = name;
        this.email = email;
        this.password = password != null && !password.isEmpty() ? password : "123456";
        this.role = role;
        this.accumulatedEarnings = 0.0;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public Double getAccumulatedEarnings() { return accumulatedEarnings; }
    public void setAccumulatedEarnings(Double accumulatedEarnings) { this.accumulatedEarnings = accumulatedEarnings; }
}
