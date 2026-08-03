package com.armi.domain.port.out;

import com.armi.model.Shift;
import com.armi.model.Store;
import com.armi.model.TimeLog;
import com.armi.model.AppUser;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ShiftPersistencePort {
    List<Shift> findAllShifts();
    Optional<Shift> findShiftById(Long id);
    Shift saveShift(Shift shift);
    void deleteShiftById(Long id);
    void deleteAllShifts();
    List<Shift> findOverlappingShifts(AppUser driver, LocalDateTime start, LocalDateTime end);
    
    Store saveStore(Store store);
    List<Store> findAllStores();
    void deleteAllStores();

    TimeLog saveTimeLog(TimeLog log);
    List<TimeLog> findAllTimeLogs();
    Optional<TimeLog> findFirstTimeLogByShiftId(Long shiftId);
    void deleteAllTimeLogs();
}
