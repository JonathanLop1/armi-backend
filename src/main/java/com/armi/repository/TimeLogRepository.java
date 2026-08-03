package com.armi.repository;

import com.armi.model.TimeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TimeLogRepository extends JpaRepository<TimeLog, Long> {
    Optional<TimeLog> findFirstByShiftIdOrderByIdDesc(Long shiftId);
    List<TimeLog> findByShiftId(Long shiftId);
}
