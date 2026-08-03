package com.armi.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "time_logs")
public class TimeLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "shift_id")
    private Shift shift;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private AppUser driver;

    private LocalDateTime actualStart;
    private LocalDateTime actualEnd;
    private Double totalEarned;

    private Long startDelayMinutes = 0L;
    private String punctualityStatus = "ON_TIME"; // ON_TIME, LATE, EARLY

    // Geolocation Audit Fields (Saved ONLY in Database)
    private Double startLatitude;
    private Double startLongitude;
    private String startCoordinates;

    public TimeLog() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Shift getShift() { return shift; }
    public void setShift(Shift shift) { this.shift = shift; }
    public AppUser getDriver() { return driver; }
    public void setDriver(AppUser driver) { this.driver = driver; }
    public LocalDateTime getActualStart() { return actualStart; }
    public void setActualStart(LocalDateTime actualStart) { this.actualStart = actualStart; }
    public LocalDateTime getActualEnd() { return actualEnd; }
    public void setActualEnd(LocalDateTime actualEnd) { this.actualEnd = actualEnd; }
    public Double getTotalEarned() { return totalEarned; }
    public void setTotalEarned(Double totalEarned) { this.totalEarned = totalEarned; }
    public Long getStartDelayMinutes() { return startDelayMinutes; }
    public void setStartDelayMinutes(Long startDelayMinutes) { this.startDelayMinutes = startDelayMinutes; }
    public String getPunctualityStatus() { return punctualityStatus; }
    public void setPunctualityStatus(String punctualityStatus) { this.punctualityStatus = punctualityStatus; }

    public Double getStartLatitude() { return startLatitude; }
    public void setStartLatitude(Double startLatitude) { this.startLatitude = startLatitude; }
    public Double getStartLongitude() { return startLongitude; }
    public void setStartLongitude(Double startLongitude) { this.startLongitude = startLongitude; }
    public String getStartCoordinates() { return startCoordinates; }
    public void setStartCoordinates(String startCoordinates) { this.startCoordinates = startCoordinates; }
}
