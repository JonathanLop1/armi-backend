package com.armi.repository;

import com.armi.model.AppUser;
import com.armi.model.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ShiftRepository extends JpaRepository<Shift, Long> {
    List<Shift> findByAssignedTo(AppUser user);
    
    // Check if user has any shifts that overlap with given times
    @Query("SELECT s FROM Shift s WHERE s.assignedTo = :user AND " +
           "(s.startTime < :end AND s.endTime > :start)")
    List<Shift> findOverlappingShifts(
            @Param("user") AppUser user, 
            @Param("start") LocalDateTime start, 
            @Param("end") LocalDateTime end);
}
