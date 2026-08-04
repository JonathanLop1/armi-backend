package com.armi.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "shifts")
public class Shift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double hourlyRate;
    private Integer availableSpots;
    private Integer durationDays = 1;
    private String payType = "HOURLY"; // "FIXED" or "HOURLY"
    
    // "available", "assigned", "in_progress", "completed"
    private String status;

    private Integer ordersCount = 0;
    private Double rate = 48000.0; // Fixed pay value e.g. 48000.0, 60000.0

    @ManyToOne
    @JoinColumn(name = "assigned_to")
    private AppUser assignedTo;

    public Shift() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Store getStore() { return store; }
    public void setStore(Store store) { this.store = store; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public Double getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(Double hourlyRate) { this.hourlyRate = hourlyRate; }
    public Integer getAvailableSpots() { return availableSpots; }
    public void setAvailableSpots(Integer availableSpots) { this.availableSpots = availableSpots; }
    public Integer getDurationDays() { return durationDays; }
    public void setDurationDays(Integer durationDays) { this.durationDays = durationDays; }
    public String getPayType() { return payType; }
    public void setPayType(String payType) { this.payType = payType; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getOrdersCount() { return ordersCount; }
    public void setOrdersCount(Integer ordersCount) { this.ordersCount = ordersCount; }
    public Double getRate() { return rate; }
    public void setRate(Double rate) { this.rate = rate; }
    public AppUser getAssignedTo() { return assignedTo; }
    public void setAssignedTo(AppUser assignedTo) { this.assignedTo = assignedTo; }
}
