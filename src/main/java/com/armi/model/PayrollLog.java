package com.armi.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payroll_logs")
public class PayrollLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String weekRange;   // e.g. "27 al 02 de Agosto"
    private String driverName;  // e.g. "Jean Carlos zapata pulgarin"
    private String driverEmail; // e.g. "551004735386"
    private String storeName;   // e.g. "KFC Centro Pereira"
    private String shiftHours;  // e.g. "11:00 - 15:00"
    private Integer completedOrdersCount; // e.g. 4
    private Double payValue;    // e.g. 48000.0
    private LocalDateTime dateCompleted;

    public PayrollLog() {
        this.dateCompleted = LocalDateTime.now();
    }

    public PayrollLog(String weekRange, String driverName, String driverEmail, String storeName, String shiftHours, Integer completedOrdersCount, Double payValue) {
        this.weekRange = weekRange;
        this.driverName = driverName;
        this.driverEmail = driverEmail;
        this.storeName = storeName;
        this.shiftHours = shiftHours;
        this.completedOrdersCount = completedOrdersCount != null ? completedOrdersCount : 0;
        this.payValue = payValue != null ? payValue : 0.0;
        this.dateCompleted = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getWeekRange() { return weekRange; }
    public void setWeekRange(String weekRange) { this.weekRange = weekRange; }

    public String getDriverName() { return driverName; }
    public void setDriverName(String driverName) { this.driverName = driverName; }

    public String getDriverEmail() { return driverEmail; }
    public void setDriverEmail(String driverEmail) { this.driverEmail = driverEmail; }

    public String getStoreName() { return storeName; }
    public void setStoreName(String storeName) { this.storeName = storeName; }

    public String getShiftHours() { return shiftHours; }
    public void setShiftHours(String shiftHours) { this.shiftHours = shiftHours; }

    public Integer getCompletedOrdersCount() { return completedOrdersCount; }
    public void setCompletedOrdersCount(Integer completedOrdersCount) { this.completedOrdersCount = completedOrdersCount; }

    public Double getPayValue() { return payValue; }
    public void setPayValue(Double payValue) { this.payValue = payValue; }

    public LocalDateTime getDateCompleted() { return dateCompleted; }
    public void setDateCompleted(LocalDateTime dateCompleted) { this.dateCompleted = dateCompleted; }
}
