package com.armi.repository;

import com.armi.model.PayrollLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PayrollLogRepository extends JpaRepository<PayrollLog, Long> {
    List<PayrollLog> findByWeekRange(String weekRange);
    List<PayrollLog> findByDriverEmail(String driverEmail);
}
