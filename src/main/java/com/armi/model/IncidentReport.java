package com.armi.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "incident_reports")
public class IncidentReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reason;      // e.g. "Lluvia Fuerte", "Accidente", "Demora en Cocina"
    private String description; // Detailed description
    private LocalDateTime reportedAt;
    private String status;      // e.g. "PENDING", "RESOLVED"

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "driver_id")
    private AppUser driver;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shift_id")
    private Shift shift;

    public IncidentReport() {
        this.reportedAt = LocalDateTime.now();
        this.status = "PENDING";
    }

    public IncidentReport(String reason, String description, AppUser driver, Shift shift) {
        this.reason = reason;
        this.description = description;
        this.driver = driver;
        this.shift = shift;
        this.reportedAt = LocalDateTime.now();
        this.status = "PENDING";
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getReportedAt() { return reportedAt; }
    public void setReportedAt(LocalDateTime reportedAt) { this.reportedAt = reportedAt; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public AppUser getDriver() { return driver; }
    public void setDriver(AppUser driver) { this.driver = driver; }

    public Shift getShift() { return shift; }
    public void setShift(Shift shift) { this.shift = shift; }
}
