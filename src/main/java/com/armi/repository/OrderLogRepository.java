package com.armi.repository;

import com.armi.model.AppUser;
import com.armi.model.OrderLog;
import com.armi.model.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderLogRepository extends JpaRepository<OrderLog, Long> {
    List<OrderLog> findByDriver(AppUser driver);
    List<OrderLog> findByShift(Shift shift);
    List<OrderLog> findByStoreNameContainingIgnoreCase(String storeName);
}
