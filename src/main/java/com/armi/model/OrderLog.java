package com.armi.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_logs")
public class OrderLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderNumber; // e.g. "#522504"
    private String status;      // e.g. "Finalizada", "En Camino"
    private String orderTime;   // e.g. "16:25"
    private Integer durationMinutes; // e.g. 25
    private String storeName;  // e.g. "Pastel de Oro sede principal"
    private String channel;    // e.g. "T en Línea"
    private String clientName; // e.g. "Elsa jimenez"

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "driver_id")
    private AppUser driver;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shift_id")
    private Shift shift;

    public OrderLog() {
        this.createdAt = LocalDateTime.now();
    }

    public OrderLog(String orderNumber, String status, String orderTime, Integer durationMinutes, String storeName, String channel, String clientName, AppUser driver, Shift shift) {
        this.orderNumber = orderNumber;
        this.status = status;
        this.orderTime = orderTime;
        this.durationMinutes = durationMinutes;
        this.storeName = storeName;
        this.channel = channel;
        this.clientName = clientName;
        this.driver = driver;
        this.shift = shift;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getOrderTime() { return orderTime; }
    public void setOrderTime(String orderTime) { this.orderTime = orderTime; }

    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }

    public String getStoreName() { return storeName; }
    public void setStoreName(String storeName) { this.storeName = storeName; }

    public String getChannel() { return channel; }
    public void setChannel(String channel) { this.channel = channel; }

    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public AppUser getDriver() { return driver; }
    public void setDriver(AppUser driver) { this.driver = driver; }

    public Shift getShift() { return shift; }
    public void setShift(Shift shift) { this.shift = shift; }
}
